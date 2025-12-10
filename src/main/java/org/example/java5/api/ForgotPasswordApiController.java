//package org.example.java5.api;
//
//import org.example.java5.Service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/quenmatkhau/api")
//public class ForgotPasswordApiController {
//
//    @Autowired
//    private UserService userService;
//
//    // API: Gửi email đặt lại mật khẩu
//    @PostMapping("/send-reset-email")
//    public ResponseEntity<Map<String, Object>> sendResetPasswordEmailApi(@RequestParam String email) {
//        Map<String, Object> response = new HashMap<>();
//        userService.sendResetPasswordEmail(email);
//        response.put("status", "success");
//        response.put("message", "Kiểm tra email của bạn để đặt lại mật khẩu.");
//        return ResponseEntity.ok(response);
//    }
//
//    // API: Đặt lại mật khẩu
//    @PostMapping("/reset-password")
//    public ResponseEntity<Map<String, Object>> resetPasswordApi(@RequestParam String token, @RequestParam String password) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (userService.validateResetToken(token)) {
//            userService.resetPassword(token, password);
//            response.put("status", "success");
//            response.put("message", "Mật khẩu đã được đặt lại thành công.");
//            response.put("redirect", "/auth/login");
//            return ResponseEntity.ok(response);
//        } else {
//            response.put("status", "error");
//            response.put("message", "Token không hợp lệ hoặc đã hết hạn.");
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//}