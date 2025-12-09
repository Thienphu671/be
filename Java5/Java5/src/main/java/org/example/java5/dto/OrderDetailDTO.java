
//package org.example.java5.dto;
//
//import org.example.java5.entity.OrderDetail;
//
//import java.time.format.DateTimeFormatter;
//
//public class OrderDetailDTO {
//    private String email;
//    private String orderDate;
//    private String address;
//    private String status;
//    private String productImage;
//    private String productName;
//
//    public OrderDetailDTO(OrderDetail orderDetail) {
//        // Lấy thông tin người dùng
//        this.email = orderDetail.getOrder().getUser().getEmail();
//
//        // Lấy thông tin đơn hàng
//        this.address = orderDetail.getOrder().getAddress();
//
//        // Chuyển trạng thái từ int sang mô tả
//        int rawStatus = orderDetail.getOrder().getStatus(); // int: 0, 1, 2...
//        this.status = convertStatusToText(rawStatus);
//
//        // Format ngày đặt hàng (giả sử createdAt là LocalDateTime)
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        // Lấy thông tin sản phẩm
//        this.productImage = orderDetail.getProduct().getHinh(); // đường dẫn ảnh
//        this.productName = orderDetail.getProduct().getTen();
//    }
//
//    // Chuyển status int → chuỗi mô tả
//    private String convertStatusToText(int statusCode) {
//        switch (statusCode) {
//            case 0: return "Chưa thanh toán";
//            case 1: return "Đã thanh toán";
//            case 2: return "Đã hủy";
//            default: return "Không xác định";
//        }
//    }
//
//    // Getters & Setters
//
//
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getOrderDate() {
//        return orderDate;
//    }
//
//    public void setOrderDate(String orderDate) {
//        this.orderDate = orderDate;
//    }
//
//    public String getProductImage() {
//        return productImage;
//    }
//
//    public void setProductImage(String productImage) {
//        this.productImage = productImage;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//}


package org.example.java5.dto;

import org.example.java5.entity.OrderDetail;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderDetailDTO {
    private String customerName;
    private String email;
    private String phoneNumber;
    private String address;
    private Date orderDate;
    private String status;
    private String productImage;
    private String productName;
    private int quantity;
    private double price;
    private List<OrderDetailItemDTO> orderDetails;
    private Integer productId;

    public OrderDetailDTO(OrderDetail orderDetail) {
        // Lấy thông tin người dùng
        this.customerName = orderDetail.getOrder().getUser().getFullname();
        this.email = orderDetail.getOrder().getUser().getEmail();
        this.phoneNumber = orderDetail.getOrder().getUser().getPhoneNumber();

        // Lấy thông tin đơn hàng
        this.address = orderDetail.getOrder().getAddress();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int rawStatus = orderDetail.getOrder().getStatus();
        this.status = convertStatusToText(rawStatus);

        // Lấy thông tin sản phẩm
        this.productImage = orderDetail.getProduct().getHinh();
        this.productName = orderDetail.getProduct().getTen();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.productId = orderDetail.getProduct().getId(); // <- gán id sản phẩm
    }

    public OrderDetailDTO() {
    }

    public List<OrderDetailItemDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailItemDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public OrderDetailDTO(String customerName, String email, String phoneNumber, String address, Date orderDate, String status, String productImage, String productName, int quantity, double price, List<OrderDetailItemDTO> orderDetails, Integer productId) {
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderDate = orderDate;
        this.status = status;
        this.productImage = productImage;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.orderDetails = orderDetails;
        this.productId = productId;
    }

    private String convertStatusToText(int statusCode) {
        switch (statusCode) {
            case 0: return "Chưa thanh toán";
            case 1: return "Đã thanh toán";
            case 2: return "Đã hủy";
            default: return "Không xác định";
        }
    }

    // Getters & Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

