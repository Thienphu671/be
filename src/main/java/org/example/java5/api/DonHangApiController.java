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

import java.util.*;
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
            token = token.substring(7);
            String email = jwtUtil.extractEmail(token);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Lấy danh sách đơn hàng của user hiện tại (hoặc tất cả nếu là admin - tùy chỉnh sau)
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

    // Chi tiết đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int id) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        OrderDTO orderDTO = new OrderDTO(order);

        List<OrderDetailDTO> details = order.getOrderDetails().stream()
                .map(OrderDetailDTO::new)
                .collect(Collectors.toList());

        // Tính tổng tiền trước
        double totalPrice = order.getOrderDetails().stream()
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();

        // Tạo object với các giá trị đã tính sẵn
        Map<String, Object> response = new HashMap<>();
        response.put("order", orderDTO);
        response.put("totalPrice", totalPrice);
        response.put("orderDetails", details);

        return ResponseEntity.ok(response);
    }

    // Xác nhận đơn hàng (admin)
    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmOrder(@PathVariable("id") int id) {
        orderService.confirmOrder(id);
        return ResponseEntity.ok("Đơn hàng đã được xác nhận!");
    }

    // Hủy đơn hàng (chỉ khi chưa xác nhận) + cộng lại tồn kho
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable("id") int id) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();

        // Giả sử status = 0 là "Chờ xác nhận" (chưa xác nhận)
        if (order.getStatus() != 0) {
            return ResponseEntity.badRequest().body("Chỉ có thể hủy đơn hàng chưa được xác nhận.");
        }

        // Cộng lại số lượng tồn kho
        for (OrderDetail item : order.getOrderDetails()) {
            Optional<Product> optionalProduct = productService.getById(item.getProduct().getId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setSoluong(product.getSoluong() + item.getQuantity());
                productService.updateProductQuantity(product);
            }
        }

        // Cập nhật trạng thái thành hủy (giả sử 2 = Đã hủy)
        order.setStatus(2);
        orderService.updateOrder(order);

        return ResponseEntity.ok("Đơn hàng đã được hủy và số lượng sản phẩm đã được khôi phục.");
    }

    // Tạo đơn hàng từ giỏ hàng + TRỪ NGAY TỒN KHO
    @PostMapping("/create-from-cart")
    public ResponseEntity<?> createOrderFromCart(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {

        Optional<User> userOpt = getUserFromToken(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập!");
        }
        User user = userOpt.get();

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
            List<Giohang> cartItems = giohangRepository.findAllById(giohangIds);

            // Kiểm tra quyền sở hữu
            boolean validOwnership = cartItems.stream()
                    .allMatch(item -> Objects.equals(item.getTaikhoan().getId(), user.getId()));

            if (!validOwnership || cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("Một số sản phẩm không hợp lệ hoặc không thuộc về bạn!");
            }

            // === KIỂM TRA SẢN PHẨM NGƯNG BÁN HOẶC HẾT HÀNG ===
            for (Giohang item : cartItems) {
                Product product = item.getSanpham();
                int requestedQty = item.getSoLuong();

                // TODO: Thay đổi điều kiện dưới đây tùy theo tên trường và giá trị trong entity Product của bạn
                // Ví dụ 1: Nếu có trường trangThai (1 = đang bán, 0 = ngưng bán)
                if (product.getStatus() == 2 || product.getStatus() == 1) {
                    return ResponseEntity.badRequest()
                            .body("Sản phẩm '" + product.getTen() + "' đã ngưng bán, hoặc hết hàng!");
                }

                // Ví dụ 2: Nếu có trường Boolean isActive
                // if (!product.isActive()) { ... }

                // Ví dụ 3: Nếu có trường String status = "active" / "inactive"
                // if (!"active".equals(product.getStatus())) { ... }

                // Kiểm tra tồn kho
                if (product.getSoluong() < requestedQty) {
                    return ResponseEntity.badRequest()
                            .body("Sản phẩm '" + product.getTen() + "' không đủ hàng! Chỉ còn " + product.getSoluong() + " sản phẩm.");
                }

                // Trừ tồn kho
                product.setSoluong(product.getSoluong() - requestedQty);
                productService.updateProductQuantity(product);
            }

            // Tạo đơn hàng sau khi tất cả kiểm tra đều thành công
            Order order = orderService.createOrder(user, cartItems, diaChi, sdt);

            // Xóa các mục khỏi giỏ hàng
            giohangRepository.deleteAll(cartItems);

            // Tính tổng tiền
            double totalAmount = cartItems.stream()
                    .mapToDouble(item -> item.getSanpham().getGia().doubleValue() * item.getSoLuong())
                    .sum();

            return ResponseEntity.ok(Map.of(
                    "message", "Tạo đơn hàng thành công! Đã trừ số lượng sản phẩm khỏi kho.",
                    "orderId", order.getId(),
                    "totalAmount", totalAmount
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }
}