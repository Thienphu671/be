// src/main/java/org/example/java5/dto/GiohangReponse.java

package org.example.java5.dto;

import java.math.BigDecimal;

public class GiohangReponse {

    private Integer id;
    private Integer userId;
    private Integer sanphamId;
    private String tenSanPham;
    private String hinhAnh;        // tên file ảnh
    private Integer quantity;
    private BigDecimal giaSanPham; // giá 1 sản phẩm
    private BigDecimal totalPrice; // tổng tiền = gia * quantity

    // Constructor
    public GiohangReponse(Integer id, Integer userId, Integer sanphamId, String tenSanPham,
                          String hinhAnh, Integer quantity, BigDecimal giaSanPham, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.sanphamId = sanphamId;
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
        this.quantity = quantity;
        this.giaSanPham = giaSanPham;
        this.totalPrice = totalPrice;
    }

    // Getters (bắt buộc)
    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public Integer getSanphamId() { return sanphamId; }
    public String getTenSanPham() { return tenSanPham; }
    public String getHinhAnh() { return hinhAnh; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getGiaSanPham() { return giaSanPham; }
    public BigDecimal getTotalPrice() { return totalPrice; }
}