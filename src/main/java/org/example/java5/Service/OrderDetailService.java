package org.example.java5.Service;

import org.example.java5.entity.OrderDetail;
import org.example.java5.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public Optional<OrderDetail> getOrderDetailById(int id) {
        return orderDetailRepository.findById(id);
    }

    public Optional<OrderDetail> getOrderDetailById(Integer orderDetailId) {
        return orderDetailRepository.findById(orderDetailId);
    }

    //admin


    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public void deleteOrderDetail(int id) {
        orderDetailRepository.deleteById(id);
    }






}
