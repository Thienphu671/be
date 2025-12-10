//package org.example.java5.api;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.example.java5.beans.doimatkhauBean;
//import org.example.java5.dto.ChangePasswordResponse;
//import org.example.java5.entity.User;
//import org.example.java5.Service.UserService;
//import org.example.java5.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/doiMatKhau/api")
//public class ChangePasswordApiController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/submit")
//    public ResponseEntity<ChangePasswordResponse> doiMatKhauApi(
//            @ModelAttribute @Valid doimatkhauBean form,
//            BindingResult result,
//            HttpSession session) {
//
//        if (result.hasErrors()) {
//            Map<String, String> errors = new HashMap<>();
//            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
//            return ResponseEntity.badRequest()
//                    .body(new ChangePasswordResponse("error", "Dữ liệu không hợp lệ: " + errors.toString(), null));
//        }
//
//        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
//
//        if (loggedInUserEmail == null) {
//            return ResponseEntity.status(401)
//                    .body(new ChangePasswordResponse("error", "Bạn chưa đăng nhập!", "/auth/login"));
//        }
//
//        Optional<User> userOpt = userRepository.findByEmail(loggedInUserEmail);
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.badRequest()
//                    .body(new ChangePasswordResponse("error", "Tài khoản không tồn tại!", "/auth/login"));
//        }
//
//        User user = userOpt.get();
//        boolean isChanged = userService.changePassword(user.getEmail(), form.getOldPassword(), form.getNewPassword());
//
//        if (isChanged) {
//            return ResponseEntity.ok(
//                    new ChangePasswordResponse("success", "Đổi mật khẩu thành công!", "/auth/login"));
//        } else {
//            return ResponseEntity.badRequest()
//                    .body(new ChangePasswordResponse("error", "Mật khẩu cũ không đúng!", null));
//        }
//    }
//}