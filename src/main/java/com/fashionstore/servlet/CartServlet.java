package com.fashionstore.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartDAOImpl;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        CartDAO cartDAO = new CartDAOImpl();
        List<CartItem> items = cartDAO.getCartItems(user.getUserId());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        request.setAttribute("cartItems", items);
        request.setAttribute("cartTotal", total);
        request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        User user = (User) request.getSession().getAttribute("user");
        String action = request.getParameter("action");
        CartDAO cartDAO = new CartDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();

        if ("add".equals(action)) {
            int variantId = Integer.parseInt(request.getParameter("variantId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (productDAO.getVariantStock(variantId) >= quantity) {
                cartDAO.addToCart(user.getUserId(), variantId, quantity);
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        } else if ("update".equals(action)) {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            List<CartItem> items = cartDAO.getCartItems(user.getUserId());
            for (CartItem item : items) {
                if (item.getCartId() == cartId) {
                    if (productDAO.getVariantStock(item.getVariantId()) >= quantity) {
                        cartDAO.updateQuantity(cartId, user.getUserId(), quantity);
                    }
                    break;
                }
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        } else if ("remove".equals(action)) {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            cartDAO.removeFromCart(cartId, user.getUserId());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
