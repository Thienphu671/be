package org.example.java5.dto;

import org.example.java5.entity.Product;

import java.math.BigDecimal;

public class GiohangReponse {
    private int id;
    private int userId;
    private Product product;
    private int quantity;
    private BigDecimal totalPrice ;

    // Constructor
    public GiohangReponse(int id, int userId, int productId, String pn, int quantity, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.product = new Product(productId);
        this.product.setTen(pn);
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }



    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
