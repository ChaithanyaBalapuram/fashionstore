package com.fashionstore.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.CategoryDAOImpl;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductDetail;
import com.fashionstore.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.requireAdmin(request, response)) return;

        ProductDAO productDAO = new ProductDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();

        request.setAttribute("products", productDAO.getAllProductsAdmin());
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.requireAdmin(request, response)) return;

        String action = request.getParameter("action");
        ProductDAO productDAO = new ProductDAOImpl();

        if ("add".equals(action)) {
            Product product = buildProduct(request);
            int productId = productDAO.addProduct(product);
            if (productId > 0) {
                ProductVariant variant = new ProductVariant();
                variant.setProductId(productId);
                variant.setColor(request.getParameter("color"));
                variant.setSize(request.getParameter("size"));
                variant.setStock(product.getStock());
                productDAO.addVariant(variant);

                ProductDetail detail = new ProductDetail();
                detail.setProductId(productId);
                detail.setGender(request.getParameter("gender"));
                detail.setMaterial(request.getParameter("material"));
                detail.setFitType(request.getParameter("fitType"));
                detail.setCareInstructions(request.getParameter("careInstructions"));
                detail.setCountryOfOrigin(request.getParameter("countryOfOrigin"));
                productDAO.saveProductDetail(detail);
            }
        } else if ("delete".equals(action)) {
            productDAO.deleteProduct(Integer.parseInt(request.getParameter("productId")));
        } else if ("reactivate".equals(action)) {
            productDAO.reactivateProduct(Integer.parseInt(request.getParameter("productId")));
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    private Product buildProduct(HttpServletRequest request) {
        Product product = new Product();
        product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        product.setProductName(request.getParameter("productName"));
        product.setBrand(request.getParameter("brand"));
        product.setDescription(request.getParameter("description"));
        product.setOriginalPrice(new BigDecimal(request.getParameter("originalPrice")));
        String discount = request.getParameter("discountPrice");
        if (discount != null && !discount.isEmpty()) {
            product.setDiscountPrice(new BigDecimal(discount));
        }
        product.setStock(Integer.parseInt(request.getParameter("stock")));
        product.setFeatured("on".equals(request.getParameter("featured")));
        product.setNewArrival("on".equals(request.getParameter("newArrival")));
        product.setActive(true);
        return product;
    }
}
