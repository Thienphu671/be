package org.example.java5.controllers.nguoiDung;

import org.example.java5.Service.SanPhamService;
import org.example.java5.dto.OrderDTO;
import org.example.java5.dto.OrderDetailDTO;
import org.example.java5.dto.OrderDetailResponseDTO;
import org.example.java5.dto.UserDTO;
import org.example.java5.entity.Order;
import org.example.java5.Service.OrderService;
import org.example.java5.entity.OrderDetail;
import org.example.java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

//@Controller
//public class donHangND {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private SanPhamService sanPhamService;
//
//    // Danh sách đơn hàng theo tài khoản đăng nhập
//    @GetMapping("/donHangND/form")
//    public String listOrders(@CookieValue(value = "userId", defaultValue = "0") int userId, Model model) {
//        if (userId == 0) {
//            return "redirect:/login"; // Chuyển hướng nếu chưa đăng nhập
//        }
//        List<Order> orders = orderService.getOrdersByUserId(userId); // Lấy đơn hàng theo ID user
//        orders.sort(Comparator.comparing(Order::getOrderDate).reversed()); // Sắp xếp theo ngày mới nhất
//        model.addAttribute("orders", orders);
//        return "nguoiDung/donHangND";
//    }
//
//    // Chi tiết đơn hàng
//    @GetMapping("/ordersND/{id}")
//    public String orderDetails(@PathVariable int id, Model model) {
//        Order order = orderService.getOrderById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
//
//        // Tính tổng tiền đơn hàng
//        double totalPrice = order.getOrderDetails().stream()
//                .mapToDouble(d -> d.getPrice() * d.getQuantity())
//                .sum();
//
//        model.addAttribute("order", order);
//        model.addAttribute("totalPrice", totalPrice);
//        return "nguoiDung/donHangChiTietND";
//    }
//
//    // Xác nhận đơn hàng
//    @PostMapping("/confirmND/{id}")
//    public String confirmOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
//        orderService.confirmOrder(id);
//        redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được xác nhận!");
//        return "redirect:/donHangND/form"; // Quay lại danh sách đơn hàng
//    }
//
//    // Hủy đơn hàng
//    @PostMapping("/cancelND/{id}")
//    public String cancelOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
//        // Lấy thông tin đơn hàng từ database
//        Order order = orderService.getOrderById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
//
//        // Kiểm tra nếu đơn hàng chưa được xác nhận thì mới cho hủy
//        if (order.getStatus() == 0) {
//            order.setStatus(2); // Cập nhật trạng thái thành "Đã hủy"
//            orderService.updateOrder(order); // Lưu đơn hàng
//
//            // ✅ Khôi phục số lượng sản phẩm trong bảng Product
//            for (OrderDetail item : order.getOrderDetails()) { // Duyệt qua danh sách sản phẩm trong đơn hàng
//                Optional<Product> optionalProduct = sanPhamService.getById(item.getProduct().getId());
//                if (optionalProduct.isPresent()) {
//                    Product product = optionalProduct.get();
//                    product.setSoluong(product.getSoluong() + item.getQuantity()); // Cộng lại số lượng đã đặt
//                    sanPhamService.updateProductQuantity(product); // Cập nhật lại số lượng
//                }
//            }
//
//            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được hủy và số lượng sản phẩm đã được khôi phục.");
//        } else {
//            redirectAttributes.addFlashAttribute("error", "Bạn chỉ có thể hủy đơn hàng chưa xác nhận.");
//        }
//
//        return "redirect:/donHangND/form";
//    }
//
//}


//@RestController
//@RequestMapping("/api/donHangND")
//public class donHangND {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private SanPhamService sanPhamService;
//
//    // Lấy danh sách đơn hàng theo userId
//    @GetMapping("/list")
//    public ResponseEntity<List<OrderDTO>> listOrders(@RequestParam("userId") int userId) {
//        if (userId == 0) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Chưa đăng nhập
//        }
//        List<Order> orders = orderService.getOrdersByUserId(userId); // Lấy đơn hàng theo ID user
//        orders.sort(Comparator.comparing(Order::getOrderDate).reversed()); // Sắp xếp theo ngày mới nhất
//
//        // Chuyển đổi từ Order entity sang OrderDTO
//        List<OrderDTO> orderDTOs = orders.stream()
//                .map(order -> new OrderDTO(order))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(orderDTOs);
//    }
//
//    // Chi tiết đơn hàng
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<OrderDetailDTO> orderDetails(@PathVariable int id) {
//        Order order = orderService.getOrderById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
//
//        // Tính tổng tiền đơn hàng
//        double totalPrice = order.getOrderDetails().stream()
//                .mapToDouble(d -> d.getPrice() * d.getQuantity())
//                .sum();
//
//        // Tạo Response DTO với thông tin chi tiết đơn hàng
//        OrderDetailDTO response = new OrderDetailDTO(order.getOrderDetails(), totalPrice);
//        return ResponseEntity.ok(response);
//    }
//
//    // Xác nhận đơn hàng
//    @PostMapping("/confirm/{id}")
//    public ResponseEntity<String> confirmOrder(@PathVariable("id") int id) {
//        Order order = orderService.getOrderById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
//
//        // Cập nhật trạng thái đơn hàng
//        if (order.getStatus() == 0) {  // Kiểm tra trạng thái nếu chưa xác nhận
//            order.setStatus(1); // Trạng thái "1" là đã xác nhận
//            orderService.updateOrder(order);
//
//            return ResponseEntity.ok("Đơn hàng đã được xác nhận!");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Đơn hàng đã được xác nhận hoặc không thể xác nhận nữa.");
//        }
//    }
//
//    // Hủy đơn hàng
//    @PostMapping("/cancel/{id}")
//    public ResponseEntity<String> cancelOrder(@PathVariable("id") int id) {
//        Order order = orderService.getOrderById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
//
//        // Kiểm tra nếu đơn hàng chưa được xác nhận thì mới cho hủy
//        if (order.getStatus() == 0) {
//            order.setStatus(2); // Trạng thái "2" là đã hủy
//            orderService.updateOrder(order);
//
//            // ✅ Khôi phục số lượng sản phẩm trong bảng Product
//            for (OrderDetail item : order.getOrderDetails()) {
//                Optional<Product> optionalProduct = sanPhamService.getById(item.getProduct().getId());
//                if (optionalProduct.isPresent()) {
//                    Product product = optionalProduct.get();
//                    product.setSoluong(product.getSoluong() + item.getQuantity()); // Cộng lại số lượng đã đặt
//                    sanPhamService.updateProductQuantity(product); // Cập nhật lại số lượng
//                }
//            }
//
//            return ResponseEntity.ok("Đơn hàng đã được hủy và số lượng sản phẩm đã được khôi phục.");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Bạn chỉ có thể hủy đơn hàng chưa xác nhận.");
//        }
//    }
//}

@RestController
@RequestMapping("/api/donHangND")
public class donHangND {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SanPhamService sanPhamService;

    // Lấy danh sách đơn hàng theo userId
    @GetMapping("/list")
    public ResponseEntity<List<OrderDTO>> listOrders(@RequestParam("userId") int userId) {
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Chưa đăng nhập
        }

        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Không có đơn hàng
        }

        // Sắp xếp theo ngày mới nhất
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());

        // Chuyển đổi từ Order entity sang OrderDTO
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> new OrderDTO(order))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }

    // Chi tiết đơn hàng
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<Map<String, Object>> orderDetails(@PathVariable int id) {
//        Order order = orderService.getOrderById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
//
//        // Tính tổng tiền đơn hàng
//        double totalPrice = order.getOrderDetails().stream()
//                .mapToDouble(d -> d.getPrice() * d.getQuantity())
//                .sum();
//
//        // Chuyển đổi danh sách OrderDetail sang OrderDetailDTO
//        List<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
//                .map(OrderDetailDTO::new)
//                .collect(Collectors.toList());
//
//        // Gói dữ liệu phản hồi
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", convertStatusToText(order.getStatus())); // ✅ convert int to text
//        response.put("orderDetails", new OrderDetailResponseDTO(orderDetailDTOs, totalPrice));
//        response.put("user", new UserDTO(order.getUser())); // ✅ trả về fullName + email
//
//        return ResponseEntity.ok(response);
//    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> orderDetails(@PathVariable int id) {
        // Lấy đơn hàng từ dịch vụ, nếu không có thì ném lỗi
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Tính tổng tiền đơn hàng
        double totalPrice = order.getOrderDetails().stream()
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();

        // Chuyển đổi danh sách OrderDetail sang OrderDetailDTO
        List<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
                .map(OrderDetailDTO::new)
                .collect(Collectors.toList());

        // Tạo đối tượng OrderDetailResponseDTO với danh sách OrderDetailDTO và tổng giá trị
        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO(orderDetailDTOs, totalPrice);

        // Gói dữ liệu phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("status", convertStatusToText(order.getStatus())); // Chuyển đổi trạng thái đơn hàng từ int sang text
        response.put("orderDetails", orderDetailResponseDTO); // Đưa thông tin chi tiết đơn hàng vào phản hồi
        response.put("user", new UserDTO(order.getUser())); // Trả về thông tin người dùng với fullName và email
        response.put("address", order.getAddress()); // Trả về địa chỉ từ Order

        return ResponseEntity.ok(response);
    }






    // Helper method
    private String convertStatusToText(int statusCode) {
        switch (statusCode) {
            case 0: return "Chưa thanh toán";
            case 1: return "Đã thanh toán";
            case 2: return "Đã hủy";
            default: return "Không xác định";
        }
    }


    // Xác nhận đơn hàng
    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmOrder(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Kiểm tra trạng thái nếu chưa xác nhận
        if (order.getStatus() == 0) {  // Trạng thái 0: Chưa xác nhận
            order.setStatus(1); // Trạng thái 1: Đã xác nhận
            orderService.updateOrder(order);

            return ResponseEntity.ok("Đơn hàng đã được xác nhận!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Đơn hàng đã được xác nhận hoặc không thể xác nhận nữa.");
        }
    }

    // Hủy đơn hàng
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable("id") int id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Kiểm tra nếu đơn hàng chưa được xác nhận thì mới cho hủy
        if (order.getStatus() == 0) {  // Trạng thái 0: Chưa xác nhận
            order.setStatus(2); // Trạng thái 2: Đã hủy
            orderService.updateOrder(order);

            // ✅ Khôi phục số lượng sản phẩm trong bảng Product
            for (OrderDetail item : order.getOrderDetails()) {
                Optional<Product> optionalProduct = sanPhamService.getById(item.getProduct().getId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setSoluong(product.getSoluong() + item.getQuantity()); // Cộng lại số lượng đã đặt
                    sanPhamService.updateProductQuantity(product); // Cập nhật lại số lượng
                }
            }

            return ResponseEntity.ok("Đơn hàng đã được hủy và số lượng sản phẩm đã được khôi phục.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bạn chỉ có thể hủy đơn hàng chưa xác nhận.");
        }
    }




}

