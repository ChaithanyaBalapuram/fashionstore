package com.fashionstore.dao;

import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.model.Product;
import com.fashionstore.model.ProductDetail;
import com.fashionstore.model.ProductImage;
import com.fashionstore.model.ProductVariant;

public interface ProductDAO {
    List<Product> getAllProducts(Integer categoryId, String search, String sort, BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> getAllProductsAdmin();
    List<Product> getFeaturedProducts();
    Product getProductById(int productId);
    Product getProductByIdAdmin(int productId);
    List<ProductVariant> getVariantsByProductId(int productId);
    ProductVariant getVariantById(int variantId);
    int getVariantStock(int variantId);
    ProductDetail getProductDetail(int productId);
    List<ProductImage> getProductImages(int productId);
    int addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int productId);
    boolean reactivateProduct(int productId);
    boolean addVariant(ProductVariant variant);
    boolean updateVariant(ProductVariant variant);
    boolean deleteVariant(int variantId);
    boolean saveProductDetail(ProductDetail detail);
    boolean addProductImage(int productId, String imageUrl);
}
