//package org.example.java5.controllers.nguoiDung;
//
//import org.example.java5.Service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/quenmatkhau")
//public class quenmatkhau {
//
//    @Autowired
//    private UserService userService;
//
//    // Giao diện HTML: Hiển thị trang quên mật khẩu
//    @GetMapping
//    public String showForgotPasswordPage() {
//        return "nguoiDung/forgot-password";
//    }
//
//    // Giao diện HTML: Xử lý yêu cầu gửi email đặt lại mật khẩu
//    @PostMapping
//    public String handleForgotPassword(@RequestParam String email, Model model) {
//        userService.sendResetPasswordEmail(email);
//        model.addAttribute("message", "Kiểm tra email của bạn để đặt lại mật khẩu.");
//        return "nguoiDung/forgot-password";
//    }
//
//    // Giao diện HTML: Hiển thị trang đặt lại mật khẩu
//    @GetMapping("/reset-password")
//    public String showResetPasswordPage(@RequestParam String token, Model model) {
//        if (userService.validateResetToken(token)) {
//            model.addAttribute("token", token);
//            return "nguoiDung/reset-password";
//        }
//        model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
//        return "nguoiDung/forgot-password";
//    }
//
//    // Giao diện HTML: Xử lý đặt lại mật khẩu
//    @PostMapping("/reset-password")
//    public String handleResetPassword(@RequestParam String token, @RequestParam String password, Model model) {
//        if (userService.validateResetToken(token)) {
//            userService.resetPassword(token, password);
//            model.addAttribute("message", "Mật khẩu đã được đặt lại thành công.");
//            return "nguoiDung/login";
//        }
//        model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
//        return "nguoiDung/reset-password";
//    }
//}
package org.example.java5.controllers.nguoiDung;

import org.example.java5.Service.UserService;
import org.example.java5.dto.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quenmatkhau")
public class quenmatkhau {

    @Autowired
    private UserService userService;

    // Gửi email để đặt lại mật khẩu
    @PostMapping
    public ResponseEntity<?> handleForgotPassword(@RequestParam String email) {
        try {
            userService.sendResetPasswordEmail(email);
            return ResponseEntity.ok("Kiểm tra email của bạn để đặt lại mật khẩu.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email không tồn tại hoặc có lỗi khi gửi email.");
        }
    }

    // Đặt lại mật khẩu với token
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> handleResetPassword(@RequestParam String token,
//                                                 @RequestParam String password) {
//        try {
//            boolean isValid = userService.validateResetToken(token);
//            if (isValid) {
//                userService.resetPassword(token, password);
//                return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("Token không hợp lệ hoặc đã hết hạn.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Có lỗi khi đặt lại mật khẩu.");
//        }
//    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> handleResetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String token = request.getToken();
            String password = request.getPassword();

            boolean isValid = userService.validateResetToken(token);
            if (isValid) {
                userService.resetPassword(token, password);
                return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Token không hợp lệ hoặc đã hết hạn.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi khi đặt lại mật khẩu.");
        }
    }

}
