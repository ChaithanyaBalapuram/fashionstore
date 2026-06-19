package com.fashionstore.servlet;

import java.io.IOException;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderDAOImpl;
import com.fashionstore.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.requireAdmin(request, response)) return;

        OrderDAO orderDAO = new OrderDAOImpl();
        request.setAttribute("orders", orderDAO.getAllOrders());
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.requireAdmin(request, response)) return;

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        Admin admin = (Admin) request.getSession().getAttribute("admin");

        OrderDAO orderDAO = new OrderDAOImpl();
        orderDAO.updateOrderStatus(orderId, status, admin.getUsername());

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
