package org.example.java5.controllers.nguoiDung;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.java5.Service.GiohangService;
import org.example.java5.Service.SanPhamService;
import org.example.java5.Service.UserService;
import org.example.java5.entity.Giohang;
import org.example.java5.entity.Product;
import org.example.java5.entity.User;
import org.example.java5.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GiohangService giohangService;

    @Autowired
    private UserService userService;

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session,
                           @CookieValue(value = "address", defaultValue = "") String address,
                           @CookieValue(value = "phone", defaultValue = "") String phone) {
        Integer taikhoan_id = (Integer) session.getAttribute("taikhoanid");

        if (taikhoan_id == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
        }

        // Lấy thông tin tài khoản người dùng
        User user = userService.getUserById(taikhoan_id);
        if (user == null) {
            return "redirect:/login"; // Nếu tài khoản không tồn tại, chuyển hướng đến đăng nhập
        }

        List<Giohang> cartItems = giohangService.getAllByTaikhoanId(taikhoan_id);
        List<Product> productList = cartItems.stream()
                .map(Giohang::getSanpham)
                .toList();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("products", productList);
        model.addAttribute("address", address);
        model.addAttribute("phone", user.getPhoneNumber());
        model.addAttribute("fullname", user.getFullname());
        return "nguoiDung/checkout";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam String address,
                             @RequestParam String phone,
                             HttpSession session, HttpServletResponse response) {
        Integer taikhoan_id = (Integer) session.getAttribute("taikhoanid");
        if (taikhoan_id == null) {
            return "redirect:/login";
        }

        // Lấy danh sách sản phẩm từ session
        List<Giohang> selectedItems = (List<Giohang>) session.getAttribute("selectedItems");
        if (selectedItems == null || selectedItems.isEmpty()) {
            return "redirect:/checkout?error=NoItemsSelected";
        }
        User user = userService.getUserById(taikhoan_id);
        if (user == null) {
            return "redirect:/login";
        }

        // ✅ Gọi phương thức tạo đơn hàng
        orderService.createOrder(user, selectedItems, address, phone);

        // ✅ Cập nhật số lượng sản phẩm trong bảng Product trước khi xóa giỏ hàng
        for (Giohang item : selectedItems) {
            Optional<Product> optionalProduct = sanPhamService.getById(item.getSanpham().getId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get(); // Lấy đối tượng Product
                int newQuantity = product.getSoluong() - item.getSoLuong();
                product.setSoluong(Math.max(newQuantity, 0)); // Đảm bảo không giảm xuống âm
                sanPhamService.updateProductQuantity(product); // Cập nhật lại số lượng
            }
        }


        // Xóa sản phẩm đã chọn khỏi giỏ hàng
        giohangService.removeSelectedItems(selectedItems.stream().map(Giohang::getId).toList());

        // Lưu thông tin địa chỉ và số điện thoại vào cookie
        try {


            Cookie phoneCookie = new Cookie("phone", URLEncoder.encode(phone, StandardCharsets.UTF_8));
            phoneCookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(phoneCookie);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/order/success";
    }




    @PostMapping("/checkout")
    public String checkout(@RequestParam("selectedProductIds") String selectedProductIds, Model model, HttpSession session,
                           @CookieValue(value = "address", defaultValue = "") String address,
                           @CookieValue(value = "phone", defaultValue = "") String phone) {

        if (selectedProductIds.isEmpty()) {
            return "redirect:/giohang?error=NoProductSelected";
        }

        Integer taikhoan_id = (Integer) session.getAttribute("taikhoanid");
        if (taikhoan_id == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(taikhoan_id);
        if (user == null) {
            return "redirect:/login";
        }

        List<Integer> productIds = Arrays.stream(selectedProductIds.split(","))
                .map(Integer::parseInt)
                .toList();

        List<Giohang> selectedItems = giohangService.getCartItemsByIds(productIds);

        // 🔹 Lưu danh sách sản phẩm vào session
        session.setAttribute("selectedItems", selectedItems);

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("totalPrice", giohangService.tinhTongTien(selectedItems));
        model.addAttribute("address", address);
        model.addAttribute("phone", user.getPhoneNumber());
        model.addAttribute("fullname", user.getFullname());

        return "nguoiDung/checkout";
    }




    @GetMapping("/success")
    public String orderSuccess() {
        return "nguoiDung/order-success";
    }
}