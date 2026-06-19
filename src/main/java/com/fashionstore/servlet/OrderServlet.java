package com.fashionstore.servlet;

import java.io.IOException;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        OrderDAO orderDAO = new OrderDAOImpl();
        request.setAttribute("orders", orderDAO.getOrdersByUserId(user.getUserId()));
        request.getRequestDispatcher("/customer/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAOImpl();
        if (orderDAO.cancelOrder(orderId, user.getUserId())) {
            response.sendRedirect(request.getContextPath() + "/orders?success=cancelled");
        } else {
            response.sendRedirect(request.getContextPath() + "/orders?error=cancel");
        }
    }
}
