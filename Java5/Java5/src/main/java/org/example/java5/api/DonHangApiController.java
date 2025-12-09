package org.example.java5.api;

import org.example.java5.Service.OrderService;
import org.example.java5.Service.SanPhamService;
import org.example.java5.dto.OrderDTO;
import org.example.java5.dto.OrderDetailDTO;
import org.example.java5.entity.Order;
import org.example.java5.entity.OrderDetail;
import org.example.java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class DonHangApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SanPhamService productService;

    // ✅ Lấy danh sách đơn hàng (GET /api/orders)
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orderDTOs = orderService.getAllOrders().stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed()) // Sắp xếp theo ngày mới nhất
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    // ✅ Chi tiết đơn hàng theo ID (GET /api/orders/{id})
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int id) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        OrderDTO orderDTO = new OrderDTO(order); // ✅ Tách ra ngoài

        List<OrderDetailDTO> details = order.getOrderDetails().stream()
                .map(OrderDetailDTO::new)
                .collect(Collectors.toList());

        double tongTien = order.getOrderDetails().stream() // ✅ Dùng tên khác để tránh đụng
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();

        return ResponseEntity.ok(new Object() {
            public final OrderDTO order = orderDTO;
            public final double totalPrice = tongTien; // ✅ Dùng biến đã tính trước đó
            public final List<OrderDetailDTO> orderDetails = details;
        });
    }


    // ✅ Xác nhận đơn hàng (POST /api/orders/confirm/{id})
    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmOrder(@PathVariable("id") int id) {
        orderService.confirmOrder(id);
        return ResponseEntity.ok("Đơn hàng đã được xác nhận!");
    }

    // ✅ Hủy đơn hàng (POST /api/orders/cancel/{id})
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable("id") int id) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        if (order.getStatus() == 0) { // Chỉ hủy nếu chưa xác nhận
            order.setStatus(2); // Đã hủy
            orderService.updateOrder(order);

            // Khôi phục số lượng sản phẩm
            for (OrderDetail item : order.getOrderDetails()) {
                Optional<Product> optionalProduct = productService.getById(item.getProduct().getId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setSoluong(product.getSoluong() + item.getQuantity());
                    productService.updateProductQuantity(product);
                }
            }
            return ResponseEntity.ok("Đơn hàng đã được hủy và số lượng sản phẩm đã được khôi phục.");
        } else {
            return ResponseEntity.badRequest().body("Chỉ có thể hủy đơn hàng chưa xác nhận.");
        }
    }
}
