package org.example.java5.dto;



import org.example.java5.entity.Product;
import org.example.java5.entity.User;

import java.math.BigDecimal;
import java.util.Date;

public class YeuThichReponse {
    private int id;
    private User user;   // Chỉ lưu ID thay vì cả đối tượng User
    private Product product; // Chỉ lưu ID thay vì cả đối tượng Product
    private Date likedDate;
    private boolean status;

    public YeuThichReponse(int id, int userId, int productId, String productName,String Hinh, String productSize, BigDecimal productPrice, Date likedDate, boolean status) {
        this.id = id;
        this.user = new User(userId);  // Nếu cần chỉ ID của user, có thể khởi tạo một đối tượng User với ID
        this.product = new Product(productId);  // Tương tự cho Product
        this.product.setTen(productName);
        // Cập nhật thông tin cho Product
        this.product.setHinh(Hinh);
        this.product.setKichthuoc(productSize);
        this.product.setGia(productPrice);
        this.likedDate = likedDate;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getLikedDate() {
        return likedDate;
    }

    public void setLikedDate(Date likedDate) {
        this.likedDate = likedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}


