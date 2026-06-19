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

@WebServlet("/admin/product")
public class AdminProductEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.requireAdmin(request, response)) return;

        int productId = Integer.parseInt(request.getParameter("id"));
        ProductDAO productDAO = new ProductDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();

        Product product = productDAO.getProductByIdAdmin(productId);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        request.setAttribute("product", product);
        request.setAttribute("variants", productDAO.getVariantsByProductId(productId));
        request.setAttribute("detail", productDAO.getProductDetail(productId));
        request.setAttribute("images", productDAO.getProductImages(productId));
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("/admin/product-edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AdminDashboardServlet.requireAdmin(request, response)) return;

        int productId = Integer.parseInt(request.getParameter("productId"));
        String action = request.getParameter("action");
        ProductDAO productDAO = new ProductDAOImpl();

        if ("update".equals(action)) {
            Product product = new Product();
            product.setProductId(productId);
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
            product.setActive("on".equals(request.getParameter("active")));
            productDAO.updateProduct(product);

            ProductDetail detail = new ProductDetail();
            detail.setProductId(productId);
            detail.setGender(request.getParameter("gender"));
            detail.setMaterial(request.getParameter("material"));
            detail.setFitType(request.getParameter("fitType"));
            detail.setCareInstructions(request.getParameter("careInstructions"));
            detail.setCountryOfOrigin(request.getParameter("countryOfOrigin"));
            productDAO.saveProductDetail(detail);

        } else if ("addVariant".equals(action)) {
            ProductVariant variant = new ProductVariant();
            variant.setProductId(productId);
            variant.setColor(request.getParameter("color"));
            variant.setSize(request.getParameter("size"));
            variant.setStock(Integer.parseInt(request.getParameter("stock")));
            productDAO.addVariant(variant);

        } else if ("updateVariant".equals(action)) {
            ProductVariant variant = new ProductVariant();
            variant.setVariantId(Integer.parseInt(request.getParameter("variantId")));
            variant.setProductId(productId);
            variant.setColor(request.getParameter("color"));
            variant.setSize(request.getParameter("size"));
            variant.setStock(Integer.parseInt(request.getParameter("stock")));
            productDAO.updateVariant(variant);

        } else if ("deleteVariant".equals(action)) {
            productDAO.deleteVariant(Integer.parseInt(request.getParameter("variantId")));

        } else if ("addImage".equals(action)) {
            productDAO.addProductImage(productId, request.getParameter("imageUrl"));
        }

        response.sendRedirect(request.getContextPath() + "/admin/product?id=" + productId);
    }
}
