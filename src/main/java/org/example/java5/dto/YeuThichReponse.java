package org.example.java5.dto;

import java.util.Date;

public class YeuThichReponse {
    private int id;
    private int userId;   // Chỉ lưu ID thay vì cả đối tượng User
    private int productId; // Chỉ lưu ID thay vì cả đối tượng Product
    private Date likedDate;
    private boolean status;

    public YeuThichReponse(int id, int userId, int productId, Date likedDate, boolean status) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.likedDate = likedDate;
        this.status = status;
    }

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Date getLikedDate() {
        return likedDate;
    }

    public void setLikedDate(Date likedDate) {
        this.likedDate = likedDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
