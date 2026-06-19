package com.fashionstore.servlet;

import java.io.IOException;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderDAOImpl;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;
import com.fashionstore.model.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireAdmin(request, response)) return;

        ProductDAO productDAO = new ProductDAOImpl();
        OrderDAO orderDAO = new OrderDAOImpl();

        request.setAttribute("products", productDAO.getAllProductsAdmin());
        request.setAttribute("orders", orderDAO.getAllOrders());
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    static boolean requireAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        return true;
    }
}
