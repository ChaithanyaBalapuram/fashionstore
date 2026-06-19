package com.fashionstore.dao;

import java.util.List;

import com.fashionstore.model.CartItem;

public interface CartDAO {
    List<CartItem> getCartItems(int userId);
    boolean addToCart(int userId, int variantId, int quantity);
    boolean updateQuantity(int cartId, int userId, int quantity);
    boolean removeFromCart(int cartId, int userId);
    void clearCart(int userId);
    int getCartCount(int userId);
}
