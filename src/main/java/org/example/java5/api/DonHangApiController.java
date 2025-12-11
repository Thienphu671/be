package org.example.java5.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.java5.Service.GiohangService;
import org.example.java5.Service.OrderService;
import org.example.java5.Service.SanPhamService;
import org.example.java5.dto.OrderDTO;
import org.example.java5.dto.OrderDetailDTO;
import org.example.java5.entity.*;
import org.example.java5.jwt.JwtUtil;
import org.example.java5.repository.GiohangRepository;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class DonHangApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SanPhamService productService;

    @Autowired
    private GiohangService giohangService;

    @Autowired
    private GiohangRepository giohangRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private Optional<User> getUserFromToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return Optional.empty();
            }
            token = token.substring(7); // bỏ "Bearer "
            String email = jwtUtil.extractEmail(token);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

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

    @PostMapping("/create-from-cart")
    public ResponseEntity<?> createOrderFromCart(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {

        // Lấy user từ token (dùng lại hàm bạn đã có)
        Optional<User> userOpt = getUserFromToken(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập!");
        }
        User user = userOpt.get();

        // Lấy dữ liệu từ request
        List<Integer> giohangIds = (List<Integer>) requestBody.get("giohangIds");
        String diaChi = (String) requestBody.get("diaChi");
        String sdt = (String) requestBody.get("sdt");

        if (giohangIds == null || giohangIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Chưa chọn sản phẩm nào!");
        }
        if (diaChi == null || diaChi.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập địa chỉ giao hàng!");
        }
        if (sdt == null || sdt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập số điện thoại!");
        }

        try {
            // Lấy các mục trong giỏ hàng theo ID và kiểm tra quyền sở hữu
            List<Giohang> cartItems = giohangRepository.findAllById(giohangIds);

            // Kiểm tra tất cả sản phẩm có thuộc về user không
            boolean valid = cartItems.stream()
                    .allMatch(item -> item.getTaikhoan().getId() == user.getId());

            if (!valid || cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("Một số sản phẩm không hợp lệ!");
            }

            // Tạo đơn hàng (dùng lại method bạn đã có)
            Order order = orderService.createOrder(user, cartItems, diaChi, sdt);

            // XÓA các sản phẩm đã mua khỏi giỏ hàng
            giohangRepository.deleteAllById(giohangIds);

            return ResponseEntity.ok(Map.of(
                    "message", "Tạo đơn hàng thành công!",
                    "orderId", order.getId(),
                    "totalAmount", cartItems.stream()
                            .mapToDouble(item -> item.getSanpham().getGia().doubleValue() * item.getSoLuong())
                            .sum()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
}
