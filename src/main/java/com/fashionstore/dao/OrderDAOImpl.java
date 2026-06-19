package com.fashionstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.OrderTracking;
import com.fashionstore.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    private Order mapOrder(ResultSet rs) throws Exception {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        if (hasColumn(rs, "customer_name")) {
            o.setCustomerName(rs.getString("customer_name"));
        }
        o.setOrderNumber(rs.getString("order_number"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setPhone(rs.getString("phone"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setOrderDate(rs.getTimestamp("order_date"));
        return o;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws Exception {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setVariantId(rs.getInt("variant_id"));
        item.setProductName(rs.getString("product_name"));
        item.setColor(rs.getString("color"));
        item.setSize(rs.getString("size"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        return item;
    }

    private boolean hasColumn(ResultSet rs, String col) {
        try {
            rs.findColumn(col);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean placeOrder(Order order, List<CartItem> cartItems) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            for (CartItem cart : cartItems) {
                try (PreparedStatement stockCheck = con.prepareStatement(
                        "SELECT stock FROM product_variants WHERE variant_id = ? FOR UPDATE")) {
                    stockCheck.setInt(1, cart.getVariantId());
                    try (ResultSet rs = stockCheck.executeQuery()) {
                        if (!rs.next() || rs.getInt("stock") < cart.getQuantity()) {
                            con.rollback();
                            return false;
                        }
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO orders(user_id, order_number, total_amount, shipping_address, "
                    + "phone, order_status, payment_method) VALUES(?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getUserId());
                ps.setString(2, order.getOrderNumber());
                ps.setBigDecimal(3, order.getTotalAmount());
                ps.setString(4, order.getShippingAddress());
                ps.setString(5, order.getPhone());
                ps.setString(6, "Pending");
                ps.setString(7, order.getPaymentMethod());
                ps.executeUpdate();

                int orderId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    orderId = keys.getInt(1);
                }

                try (PreparedStatement itemPs = con.prepareStatement(
                        "INSERT INTO order_items(order_id, variant_id, quantity, price) VALUES(?,?,?,?)")) {
                    for (CartItem cart : cartItems) {
                        itemPs.setInt(1, orderId);
                        itemPs.setInt(2, cart.getVariantId());
                        itemPs.setInt(3, cart.getQuantity());
                        itemPs.setBigDecimal(4, cart.getPrice());
                        itemPs.addBatch();

                        try (PreparedStatement stockPs = con.prepareStatement(
                                "UPDATE product_variants SET stock = stock - ? WHERE variant_id = ?")) {
                            stockPs.setInt(1, cart.getQuantity());
                            stockPs.setInt(2, cart.getVariantId());
                            stockPs.executeUpdate();
                        }
                    }
                    itemPs.executeBatch();
                }

                try (PreparedStatement trackPs = con.prepareStatement(
                        "INSERT INTO order_tracking(order_id, status, remarks, updated_by) VALUES(?,?,?,?)")) {
                    trackPs.setInt(1, orderId);
                    trackPs.setString(2, "Pending");
                    trackPs.setString(3, "Order placed successfully");
                    trackPs.setString(4, "System");
                    trackPs.executeUpdate();
                }

                try (PreparedStatement clearPs = con.prepareStatement("DELETE FROM cart WHERE user_id = ?")) {
                    clearPs.setInt(1, order.getUserId());
                    clearPs.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapOrder(rs);
                    o.setItems(getOrderItems(con, o.getOrderId()));
                    o.setTracking(getOrderTracking(con, o.getOrderId()));
                    list.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Order getOrderById(int orderId) {
        Order o = null;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE order_id = ?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o = mapOrder(rs);
                    o.setItems(getOrderItems(con, orderId));
                    o.setTracking(getOrderTracking(con, orderId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT o.*, CONCAT(u.first_name, ' ', u.last_name) AS customer_name "
                     + "FROM orders o JOIN users u ON o.user_id = u.user_id "
                     + "ORDER BY o.order_date DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapOrder(rs);
                o.setItems(getOrderItems(con, o.getOrderId()));
                o.setTracking(getOrderTracking(con, o.getOrderId()));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status, String updatedBy) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String previousStatus = null;
            try (PreparedStatement check = con.prepareStatement(
                    "SELECT order_status FROM orders WHERE order_id = ? FOR UPDATE")) {
                check.setInt(1, orderId);
                try (ResultSet rs = check.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }
                    previousStatus = rs.getString("order_status");
                }
            }

            if ("Cancelled".equals(status) && !"Cancelled".equals(previousStatus)) {
                List<OrderItem> items = getOrderItems(con, orderId);
                for (OrderItem item : items) {
                    try (PreparedStatement restore = con.prepareStatement(
                            "UPDATE product_variants SET stock = stock + ? WHERE variant_id = ?")) {
                        restore.setInt(1, item.getQuantity());
                        restore.setInt(2, item.getVariantId());
                        restore.executeUpdate();
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE orders SET order_status = ? WHERE order_id = ?")) {
                ps.setString(1, status);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            }

            try (PreparedStatement trackPs = con.prepareStatement(
                    "INSERT INTO order_tracking(order_id, status, remarks, updated_by) VALUES(?,?,?,?)")) {
                trackPs.setInt(1, orderId);
                trackPs.setString(2, status);
                trackPs.setString(3, "Status updated to " + status);
                trackPs.setString(4, updatedBy);
                trackPs.executeUpdate();
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private List<OrderItem> getOrderItems(Connection con, int orderId) throws Exception {
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT oi.*, p.product_name, v.color, v.size "
                + "FROM order_items oi "
                + "JOIN product_variants v ON oi.variant_id = v.variant_id "
                + "JOIN products p ON v.product_id = p.product_id "
                + "WHERE oi.order_id = ?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }
        return items;
    }

    @Override
    public List<OrderTracking> getOrderTracking(int orderId) {
        try (Connection con = DBConnection.getConnection()) {
            return getOrderTracking(con, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<OrderTracking> getOrderTracking(Connection con, int orderId) throws Exception {
        List<OrderTracking> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT tracking_id, order_id, status, remarks, updated_by, updated_at "
                + "FROM order_tracking WHERE order_id = ? ORDER BY updated_at ASC")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderTracking t = new OrderTracking();
                    t.setTrackingId(rs.getInt("tracking_id"));
                    t.setOrderId(rs.getInt("order_id"));
                    t.setStatus(rs.getString("status"));
                    t.setRemarks(rs.getString("remarks"));
                    t.setUpdatedBy(rs.getString("updated_by"));
                    t.setUpdatedAt(rs.getTimestamp("updated_at"));
                    list.add(t);
                }
            }
        }
        return list;
    }

    @Override
    public boolean cancelOrder(int orderId, int userId) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT order_id, user_id, order_status FROM orders WHERE order_id = ? AND user_id = ?")) {
                ps.setInt(1, orderId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || !"Pending".equals(rs.getString("order_status"))) {
                        return false;
                    }
                }
            }

            List<OrderItem> items = getOrderItems(con, orderId);
            for (OrderItem item : items) {
                try (PreparedStatement restore = con.prepareStatement(
                        "UPDATE product_variants SET stock = stock + ? WHERE variant_id = ?")) {
                    restore.setInt(1, item.getQuantity());
                    restore.setInt(2, item.getVariantId());
                    restore.executeUpdate();
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE orders SET order_status = 'Cancelled' WHERE order_id = ?")) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }

            try (PreparedStatement trackPs = con.prepareStatement(
                    "INSERT INTO order_tracking(order_id, status, remarks, updated_by) VALUES(?,?,?,?)")) {
                trackPs.setInt(1, orderId);
                trackPs.setString(2, "Cancelled");
                trackPs.setString(3, "Order cancelled by customer");
                trackPs.setString(4, "Customer");
                trackPs.executeUpdate();
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
