package com.fashionstore.servlet;

import java.io.IOException;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            ProductDAO productDAO = new ProductDAOImpl();
            Product product = productDAO.getProductById(productId);

            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("variants", productDAO.getVariantsByProductId(productId));
            request.setAttribute("detail", productDAO.getProductDetail(productId));
            request.setAttribute("images", productDAO.getProductImages(productId));
            request.getRequestDispatcher("/product-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }
}
