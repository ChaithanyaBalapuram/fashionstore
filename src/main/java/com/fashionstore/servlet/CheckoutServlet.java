package com.fashionstore.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartDAOImpl;
import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderDAOImpl;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        CartDAO cartDAO = new CartDAOImpl();
        List<CartItem> items = cartDAO.getCartItems(user.getUserId());

        if (items.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }

        request.setAttribute("cartItems", items);
        request.setAttribute("cartTotal", total);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/customer/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        CartDAO cartDAO = new CartDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        List<CartItem> items = cartDAO.getCartItems(user.getUserId());

        if (items.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        for (CartItem item : items) {
            if (productDAO.getVariantStock(item.getVariantId()) < item.getQuantity()) {
                response.sendRedirect(request.getContextPath() + "/cart?error=stock");
                return;
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }

        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setOrderNumber("ORD" + System.currentTimeMillis());
        order.setTotalAmount(total);
        order.setShippingAddress(request.getParameter("shippingAddress"));
        order.setPhone(request.getParameter("phone"));
        order.setPaymentMethod("COD");

        if (order.getShippingAddress() == null || order.getShippingAddress().trim().isEmpty()
                || order.getPhone() == null || order.getPhone().trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/checkout?error=validation");
            return;
        }

        OrderDAO orderDAO = new OrderDAOImpl();
        if (orderDAO.placeOrder(order, items)) {
            response.sendRedirect(request.getContextPath() + "/orders?success=placed");
        } else {
            response.sendRedirect(request.getContextPath() + "/checkout?error=failed");
        }
    }
}
