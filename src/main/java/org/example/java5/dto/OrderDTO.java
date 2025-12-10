package org.example.java5.dto;

import org.example.java5.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    private int id;
    private String date;
    private int userId;
    private int status;  // Thêm trường status
    private List<OrderDetailDTO> orderDetails;

    // Constructor không tham số
    public OrderDTO() {
    }

    // Constructor nhận vào đối tượng Order để tạo OrderDTO
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.date = order.getOrderDate().toString(); // Chuyển đổi ngày thành chuỗi (String)
        this.userId = order.getUser().getId();
        this.status = order.getStatus();  // Lấy giá trị status từ entity
        this.orderDetails = order.getOrderDetails().stream()
                .map(OrderDetailDTO::new)  // Chuyển đổi từ OrderDetail sang OrderDetailDTO
                .collect(Collectors.toList());
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
