package org.example.java5.dto;

import java.util.List;

public class OrderDetailResponseDTO {
    private List<OrderDetailDTO> orderDetails;
    private double totalPrice;

    public OrderDetailResponseDTO(List<OrderDetailDTO> orderDetails, double totalPrice) {
        this.orderDetails = orderDetails;
        this.totalPrice = totalPrice;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
