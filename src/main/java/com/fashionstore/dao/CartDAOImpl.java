package com.fashionstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

public class CartDAOImpl implements CartDAO {

    private CartItem mapCartItem(ResultSet rs) throws Exception {
        CartItem item = new CartItem();
        item.setCartId(rs.getInt("cart_id"));
        item.setUserId(rs.getInt("user_id"));
        item.setVariantId(rs.getInt("variant_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setBrand(rs.getString("brand"));
        item.setColor(rs.getString("color"));
        item.setSize(rs.getString("size"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setThumbnailImage(rs.getString("thumbnail_image"));
        return item;
    }

    private static final String CART_QUERY =
            "SELECT c.cart_id, c.user_id, c.variant_id, c.quantity, "
            + "v.product_id, v.color, v.size, p.product_name, p.brand, p.thumbnail_image, "
            + "COALESCE(p.discount_price, p.original_price) AS price "
            + "FROM cart c "
            + "JOIN product_variants v ON c.variant_id = v.variant_id "
            + "JOIN products p ON v.product_id = p.product_id ";

    @Override
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(CART_QUERY + "WHERE c.user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCartItem(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addToCart(int userId, int variantId, int quantity) {
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement check = con.prepareStatement(
                    "SELECT cart_id, quantity FROM cart WHERE user_id=? AND variant_id=?")) {
                check.setInt(1, userId);
                check.setInt(2, variantId);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement update = con.prepareStatement(
                                "UPDATE cart SET quantity = quantity + ? WHERE cart_id = ?")) {
                            update.setInt(1, quantity);
                            update.setInt(2, rs.getInt("cart_id"));
                            return update.executeUpdate() > 0;
                        }
                    }
                }
            }
            try (PreparedStatement insert = con.prepareStatement(
                    "INSERT INTO cart(user_id, variant_id, quantity) VALUES(?,?,?)")) {
                insert.setInt(1, userId);
                insert.setInt(2, variantId);
                insert.setInt(3, quantity);
                return insert.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateQuantity(int cartId, int userId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(cartId, userId);
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE cart SET quantity = ? WHERE cart_id = ? AND user_id = ?")) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeFromCart(int cartId, int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM cart WHERE cart_id = ? AND user_id = ?")) {
            ps.setInt(1, cartId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void clearCart(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM cart WHERE user_id = ?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCartCount(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
