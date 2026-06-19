package com.fashionstore.servlet;

import java.io.IOException;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.CategoryDAOImpl;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDAO productDAO = new ProductDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();

        request.setAttribute("featuredProducts", productDAO.getFeaturedProducts());
        request.setAttribute("categories", categoryDAO.getAllCategories());

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
