package com.fashionstore.dao;

import java.util.List;

import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderTracking;

public interface OrderDAO {
    boolean placeOrder(Order order, List<CartItem> cartItems);
    List<Order> getOrdersByUserId(int userId);
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    boolean updateOrderStatus(int orderId, String status, String updatedBy);
    List<OrderTracking> getOrderTracking(int orderId);
    boolean cancelOrder(int orderId, int userId);
}
