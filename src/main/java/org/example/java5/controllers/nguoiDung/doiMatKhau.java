package org.example.java5.controllers.nguoiDung;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.java5.beans.doimatkhauBean;
import org.example.java5.dto.ChangePasswordRequest;
import org.example.java5.dto.ChangePasswordResponse;
import org.example.java5.entity.User;
import org.example.java5.Service.UserService;
import org.example.java5.jwt.JwtUtil;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//@Controller
//@RequestMapping("/doiMatKhau")
//public class doiMatKhau {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/form")
//    public String doiMatKhauForm(Model model) {
//        model.addAttribute("doimatkhauBean", new doimatkhauBean());
//        return "nguoiDung/doiMatKhau";
//    }
//
//    @PostMapping("/submit")
//    public String doiMatKhau(
//            @ModelAttribute("doimatkhauBean") @Valid doimatkhauBean form,
//            BindingResult result,
//            HttpSession session,
//            RedirectAttributes redirectAttributes,
//            Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("doimatkhauBean", form);
//            return "nguoiDung/doiMatKhau";
//        }
//
//        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
//
//        if (loggedInUserEmail == null) {
//            redirectAttributes.addFlashAttribute("message", "Bạn chưa đăng nhập!");
//            return "redirect:/auth/login"; // Điều chỉnh đường dẫn nếu cần
//        }
//
//        Optional<User> userOpt = userRepository.findByEmail(loggedInUserEmail);
//        if (userOpt.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Tài khoản không tồn tại!");
//            return "redirect:/auth/login";
//        }
//
//        User user = userOpt.get();
//        boolean isChanged = userService.changePassword(user.getEmail(), form.getOldPassword(), form.getNewPassword());
//
//        if (isChanged) {
//            redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công!");
//            redirectAttributes.addFlashAttribute("messageType", "success");
//            return "redirect:/auth/login";
//        } else {
//            redirectAttributes.addFlashAttribute("message", "Mật khẩu cũ không đúng!");
//            redirectAttributes.addFlashAttribute("messageType", "danger");
//            return "redirect:/doiMatKhau/form";
//        }
//    }
//}











import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/doiMatKhau")
public class doiMatKhau {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> doiMatKhau(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ChangePasswordRequest changePasswordRequest) {
        Map<String, Object> response = new HashMap<>();

        // Lấy token từ header Authorization
        String token = authorizationHeader.replace("Bearer ", "");

        // Trích xuất email từ token
        String email;
        try {
            email = jwtUtil.extractEmail(token);  // Lấy email từ token
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Token không hợp lệ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);  // Trả lỗi nếu token không hợp lệ
        }

        // Kiểm tra người dùng từ email (hoặc email đã được xác thực trong token)
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.put("status", "fail");
            response.put("message", "Người dùng không tồn tại");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // Nếu không tìm thấy người dùng
        }

        User user = userOptional.get();

        // Kiểm tra mật khẩu cũ, xác nhận các yêu cầu thay đổi mật khẩu
        if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
            response.put("status", "fail");
            response.put("message", "Mật khẩu cũ không đúng");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Nếu mật khẩu cũ sai
        }

        // Cập nhật mật khẩu mới
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "Đổi mật khẩu thành công");
        return ResponseEntity.ok(response);  // Trả về phản hồi thành công
    }

}
