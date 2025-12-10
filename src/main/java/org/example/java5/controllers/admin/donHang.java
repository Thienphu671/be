package org.example.java5.controllers.admin;

import org.example.java5.Service.SanPhamService;
import org.example.java5.Service.UserService;
import org.example.java5.entity.Order;
import org.example.java5.Service.OrderService;
import org.example.java5.entity.OrderDetail;
import org.example.java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class donHang {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SanPhamService productService;

    // Danh sách đơn hàng
    @GetMapping("/donHang/form")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed()); // Sắp xếp theo ngày mới nhất
        model.addAttribute("orders", orders);
        return "admin/donHang";
    }

    // Chi tiết đơn hàng
    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable int id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Tính tổng tiền đơn hàng
        double totalPrice = order.getOrderDetails().stream()
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();

        model.addAttribute("order", order);
        model.addAttribute("totalPrice", totalPrice);
        return "admin/DonHangChiTiet";
    }

    // Xác nhận đơn hàng
    @PostMapping("/confirm/{id}")
    public String confirmOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        orderService.confirmOrder(id);
        redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được xác nhận!");
        return "redirect:/donHang/form"; // Quay lại danh sách đơn hàng
    }

    // Hủy đơn hàng
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        // Lấy thông tin đơn hàng theo ID
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Kiểm tra nếu đơn hàng chưa được xác nhận thì mới có thể hủy
        if (order.getStatus() == 0) {
            order.setStatus(2); // Cập nhật trạng thái thành "Đã hủy"
            orderService.updateOrder(order); // Cập nhật trạng thái đơn hàng trong database

            // ✅ Khôi phục số lượng sản phẩm trong bảng Product
            for (OrderDetail item : order.getOrderDetails()) { // Duyệt qua danh sách sản phẩm trong đơn hàng
                Optional<Product> optionalProduct = productService.getById(item.getProduct().getId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setSoluong(product.getSoluong() + item.getQuantity()); // Cộng lại số lượng đã đặt
                    productService.updateProductQuantity(product); // Cập nhật lại số lượng trong database
                }
            }

            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được hủy và số lượng sản phẩm đã được khôi phục.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Chỉ có thể hủy đơn hàng chưa xác nhận.");
        }

        return "redirect:/donHang/form";
    }

}
