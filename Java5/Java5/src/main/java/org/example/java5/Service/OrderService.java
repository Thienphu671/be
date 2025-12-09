package org.example.java5.Service;

import jakarta.transaction.Transactional;
import org.example.java5.entity.*;
import org.example.java5.repository.OrderDetailRepository;
import org.example.java5.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // Tạo đơn hàng mới
    public Order createOrder(User user, List<Giohang> cartItems, String address, String phone) {
        if (cartItems == null || cartItems.isEmpty()) {
            return null; // Tránh lỗi nếu giỏ hàng trống
        }

        Order order = new Order();
        order.setAddress(address);
        order.setOrderDate(new Date());
        order.setStatus(0); // 0: Chờ xác nhận
        order.setUser(user);

        // Tính tổng tiền đơn hàng
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getSanpham().getGia().doubleValue() * item.getSoLuong())
                .sum();


        // Lưu đơn hàng vào database
        Order savedOrder = orderRepository.save(order);

        // Lưu chi tiết từng sản phẩm trong đơn hàng
        for (Giohang item : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProduct(item.getSanpham());
            orderDetail.setPrice(item.getSanpham().getGia().intValue());
            orderDetail.setQuantity(item.getSoLuong()); // Lấy số lượng từ giỏ hàng

            orderDetailRepository.save(orderDetail);
        }

        return savedOrder;
    }


    // Lấy danh sách đơn hàng của một user
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // Lấy danh sách đơn hàng theo userId
    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

    // Lấy tất cả đơn hàng (admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo ID
    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    // Lưu đơn hàng
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Xóa đơn hàng
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    // Xác nhận đơn hàng
    @Transactional
    public void confirmOrder(int id) {
        orderRepository.updateOrderStatus(id, 1); // 1 = Đã xác nhận
    }

    // Hủy đơn hàng
    @Transactional
    public void cancelOrder(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getStatus() == 0) { // Chỉ hủy khi đơn hàng chưa xác nhận
                orderRepository.updateOrderStatus(id, 2); // 2 = Đã hủy
            }
        }
    }
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }



}

