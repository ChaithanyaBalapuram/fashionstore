package com.fashionstore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.model.Product;
import com.fashionstore.model.ProductDetail;
import com.fashionstore.model.ProductImage;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;

public class ProductDAOImpl implements ProductDAO {

    private Product mapProduct(ResultSet rs) throws Exception {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        if (hasColumn(rs, "category_name")) {
            p.setCategoryName(rs.getString("category_name"));
        }
        p.setProductName(rs.getString("product_name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setOriginalPrice(rs.getBigDecimal("original_price"));
        p.setDiscountPrice(rs.getBigDecimal("discount_price"));
        p.setStock(rs.getInt("stock"));
        p.setFeatured(rs.getBoolean("featured"));
        p.setNewArrival(rs.getBoolean("new_arrival"));
        p.setActive(rs.getBoolean("active"));
        p.setThumbnailImage(rs.getString("thumbnail_image"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }

    private boolean hasColumn(ResultSet rs, String col) {
        try {
            rs.findColumn(col);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String resolveSort(String sort) {
        if ("price_asc".equals(sort)) {
            return "COALESCE(p.discount_price, p.original_price) ASC";
        }
        if ("price_desc".equals(sort)) {
            return "COALESCE(p.discount_price, p.original_price) DESC";
        }
        if ("name".equals(sort)) {
            return "p.product_name ASC";
        }
        return "p.created_at DESC";
    }

    @Override
    public List<Product> getAllProducts(Integer categoryId, String search, String sort,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.category_name FROM products p "
                + "JOIN categories c ON p.category_id = c.category_id "
                + "WHERE p.active = 1");
        if (categoryId != null) {
            sql.append(" AND p.category_id = ?");
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (p.product_name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?)");
        }
        if (minPrice != null) {
            sql.append(" AND COALESCE(p.discount_price, p.original_price) >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND COALESCE(p.discount_price, p.original_price) <= ?");
        }
        sql.append(" ORDER BY ").append(resolveSort(sort));

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (categoryId != null) {
                ps.setInt(idx++, categoryId);
            }
            if (search != null && !search.trim().isEmpty()) {
                String term = "%" + search.trim() + "%";
                ps.setString(idx++, term);
                ps.setString(idx++, term);
                ps.setString(idx++, term);
            }
            if (minPrice != null) {
                ps.setBigDecimal(idx++, minPrice);
            }
            if (maxPrice != null) {
                ps.setBigDecimal(idx, maxPrice);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> getAllProductsAdmin() {
        List<Product> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.*, c.category_name FROM products p "
                     + "JOIN categories c ON p.category_id = c.category_id "
                     + "ORDER BY p.created_at DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> getFeaturedProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.*, c.category_name FROM products p "
                     + "JOIN categories c ON p.category_id = c.category_id "
                     + "WHERE p.active = 1 AND p.featured = 1 ORDER BY p.created_at DESC LIMIT 8");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Product getProductById(int productId) {
        return fetchProduct("WHERE p.product_id = ? AND p.active = 1", productId);
    }

    @Override
    public Product getProductByIdAdmin(int productId) {
        return fetchProduct("WHERE p.product_id = ?", productId);
    }

    private Product fetchProduct(String whereClause, int productId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.*, c.category_name FROM products p "
                     + "JOIN categories c ON p.category_id = c.category_id " + whereClause)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductVariant> getVariantsByProductId(int productId) {
        List<ProductVariant> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT variant_id, product_id, color, size, stock "
                     + "FROM product_variants WHERE product_id = ? ORDER BY color, size")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductVariant v = new ProductVariant();
                    v.setVariantId(rs.getInt("variant_id"));
                    v.setProductId(rs.getInt("product_id"));
                    v.setColor(rs.getString("color"));
                    v.setSize(rs.getString("size"));
                    v.setStock(rs.getInt("stock"));
                    list.add(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ProductVariant getVariantById(int variantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT variant_id, product_id, color, size, stock "
                     + "FROM product_variants WHERE variant_id = ?")) {
            ps.setInt(1, variantId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProductVariant v = new ProductVariant();
                    v.setVariantId(rs.getInt("variant_id"));
                    v.setProductId(rs.getInt("product_id"));
                    v.setColor(rs.getString("color"));
                    v.setSize(rs.getString("size"));
                    v.setStock(rs.getInt("stock"));
                    return v;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getVariantStock(int variantId) {
        ProductVariant v = getVariantById(variantId);
        return v != null ? v.getStock() : 0;
    }

    @Override
    public ProductDetail getProductDetail(int productId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT detail_id, product_id, gender, material, care_instructions, "
                     + "fit_type, country_of_origin FROM product_details WHERE product_id = ?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProductDetail d = new ProductDetail();
                    d.setDetailId(rs.getInt("detail_id"));
                    d.setProductId(rs.getInt("product_id"));
                    d.setGender(rs.getString("gender"));
                    d.setMaterial(rs.getString("material"));
                    d.setCareInstructions(rs.getString("care_instructions"));
                    d.setFitType(rs.getString("fit_type"));
                    d.setCountryOfOrigin(rs.getString("country_of_origin"));
                    return d;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductImage> getProductImages(int productId) {
        List<ProductImage> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT image_id, product_id, image_url FROM product_images WHERE product_id = ?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductImage img = new ProductImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setProductId(rs.getInt("product_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    list.add(img);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int addProduct(Product product) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO products(category_id, product_name, brand, description, "
                     + "original_price, discount_price, stock, featured, new_arrival, active) "
                     + "VALUES(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getOriginalPrice());
            ps.setBigDecimal(6, product.getDiscountPrice());
            ps.setInt(7, product.getStock());
            ps.setBoolean(8, product.isFeatured());
            ps.setBoolean(9, product.isNewArrival());
            ps.setBoolean(10, product.isActive());
            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean updateProduct(Product product) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET category_id=?, product_name=?, brand=?, description=?, "
                     + "original_price=?, discount_price=?, stock=?, featured=?, new_arrival=?, active=? "
                     + "WHERE product_id=?")) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getOriginalPrice());
            ps.setBigDecimal(6, product.getDiscountPrice());
            ps.setInt(7, product.getStock());
            ps.setBoolean(8, product.isFeatured());
            ps.setBoolean(9, product.isNewArrival());
            ps.setBoolean(10, product.isActive());
            ps.setInt(11, product.getProductId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET active = 0 WHERE product_id = ?")) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean reactivateProduct(int productId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE products SET active = 1 WHERE product_id = ?")) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addVariant(ProductVariant variant) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO product_variants(product_id, color, size, stock) VALUES(?,?,?,?)")) {
            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getColor());
            ps.setString(3, variant.getSize());
            ps.setInt(4, variant.getStock());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateVariant(ProductVariant variant) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE product_variants SET color=?, size=?, stock=? WHERE variant_id=?")) {
            ps.setString(1, variant.getColor());
            ps.setString(2, variant.getSize());
            ps.setInt(3, variant.getStock());
            ps.setInt(4, variant.getVariantId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteVariant(int variantId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM product_variants WHERE variant_id = ?")) {
            ps.setInt(1, variantId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveProductDetail(ProductDetail detail) {
        ProductDetail existing = getProductDetail(detail.getProductId());
        try (Connection con = DBConnection.getConnection()) {
            if (existing == null) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO product_details(product_id, gender, material, care_instructions, "
                        + "fit_type, country_of_origin) VALUES(?,?,?,?,?,?)")) {
                    ps.setInt(1, detail.getProductId());
                    ps.setString(2, detail.getGender());
                    ps.setString(3, detail.getMaterial());
                    ps.setString(4, detail.getCareInstructions());
                    ps.setString(5, detail.getFitType());
                    ps.setString(6, detail.getCountryOfOrigin());
                    return ps.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE product_details SET gender=?, material=?, care_instructions=?, "
                        + "fit_type=?, country_of_origin=? WHERE product_id=?")) {
                    ps.setString(1, detail.getGender());
                    ps.setString(2, detail.getMaterial());
                    ps.setString(3, detail.getCareInstructions());
                    ps.setString(4, detail.getFitType());
                    ps.setString(5, detail.getCountryOfOrigin());
                    ps.setInt(6, detail.getProductId());
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addProductImage(int productId, String imageUrl) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO product_images(product_id, image_url) VALUES(?,?)")) {
            ps.setInt(1, productId);
            ps.setString(2, imageUrl);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
