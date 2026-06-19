package com.fashionstore.model;

public class ProductVariant {

    private int variantId;
    private int productId;
    private String color;
    private String size;
    private int stock;

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
