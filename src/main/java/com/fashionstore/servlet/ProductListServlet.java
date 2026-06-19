package com.fashionstore.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.CategoryDAOImpl;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryParam = request.getParameter("category");
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String minPriceParam = request.getParameter("minPrice");
        String maxPriceParam = request.getParameter("maxPrice");

        Integer categoryId = null;
        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryParam);
            } catch (NumberFormatException e) {
                categoryId = null;
            }
        }

        BigDecimal minPrice = parsePrice(minPriceParam);
        BigDecimal maxPrice = parsePrice(maxPriceParam);

        ProductDAO productDAO = new ProductDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();

        request.setAttribute("products", productDAO.getAllProducts(categoryId, search, sort, minPrice, maxPrice));
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("search", search);
        request.setAttribute("sort", sort);
        request.setAttribute("minPrice", minPriceParam);
        request.setAttribute("maxPrice", maxPriceParam);

        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    private BigDecimal parsePrice(String value) {
        if (value != null && !value.trim().isEmpty()) {
            return new BigDecimal(value.trim());
        }
        return null;
    }
}
