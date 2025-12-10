//package org.example.java5.api;
//
//import jakarta.servlet.http.HttpSession;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/thongtin/api")
//public class ProfileApiController {
//    private final UserRepository userRepository;
//
//    public ProfileApiController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    // API kiểm tra trùng số điện thoại
//    @GetMapping("/check-phone")
//    public ResponseEntity<Map<String, Object>> checkPhone(
//            @RequestParam("phone") String phone,
//            HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (phone == null || phone.trim().isEmpty()) {
//            response.put("status", "error");
//            response.put("message", "Số điện thoại không được để trống!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        if (!phone.matches("\\d{10,15}")) {
//            response.put("status", "error");
//            response.put("message", "Số điện thoại phải từ 10 đến 15 chữ số!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        User currentUser = (User) session.getAttribute("user");
//        if (currentUser == null) {
//            response.put("status", "error");
//            response.put("message", "Bạn chưa đăng nhập!");
//            return ResponseEntity.status(401).body(response);
//        }
//
//        boolean exists = userRepository.existsByPhoneNumber(phone) &&
//                !phone.equals(currentUser.getPhoneNumber());
//        response.put("exists", exists);
//        response.put("status", "success");
//        response.put("message", exists ? "Số điện thoại đã được sử dụng!" : "Số điện thoại hợp lệ.");
//
//        return ResponseEntity.ok(response);
//    }
//
//    // API kiểm tra trùng email
//    @GetMapping("/check-email")
//    public ResponseEntity<Map<String, Object>> checkEmail(
//            @RequestParam("email") String email,
//            HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (email == null || email.trim().isEmpty()) {
//            response.put("status", "error");
//            response.put("message", "Email không được để trống!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//            response.put("status", "error");
//            response.put("message", "Email không đúng định dạng!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        User currentUser = (User) session.getAttribute("user");
//        if (currentUser == null) {
//            response.put("status", "error");
//            response.put("message", "Bạn chưa đăng nhập!");
//            return ResponseEntity.status(401).body(response);
//        }
//
//        boolean exists = userRepository.existsByEmail(email) &&
//                !email.equals(currentUser.getEmail());
//        response.put("exists", exists);
//        response.put("status", "success");
//        response.put("message", exists ? "Email đã được sử dụng!" : "Email hợp lệ.");
//
//        return ResponseEntity.ok(response);
//    }
//
//    // API kiểm tra trùng fullname
//    @GetMapping("/check-fullname")
//    public ResponseEntity<Map<String, Object>> checkFullname(
//            @RequestParam("fullname") String fullname,
//            HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (fullname == null || fullname.trim().isEmpty()) {
//            response.put("status", "error");
//            response.put("message", "Họ tên không được để trống!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        if (fullname.length() < 2 || fullname.length() > 50) {
//            response.put("status", "error");
//            response.put("message", "Họ tên phải từ 2 đến 50 ký tự!");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        User currentUser = (User) session.getAttribute("user");
//        if (currentUser == null) {
//            response.put("status", "error");
//            response.put("message", "Bạn chưa đăng nhập!");
//            return ResponseEntity.status(401).body(response);
//        }
//
//        // Nếu mọi thứ hợp lệ, có thể trả về trạng thái thành công
//        response.put("status", "success");
//        response.put("message", "Họ tên hợp lệ!");
//        return ResponseEntity.ok(response);
//    }
//}
