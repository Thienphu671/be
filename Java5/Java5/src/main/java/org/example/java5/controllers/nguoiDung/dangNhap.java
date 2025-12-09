//package org.example.java5.controllers.nguoiDung;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/auth")
//public class dangNhap {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Giao diện HTML: Hiển thị form đăng nhập
//    @GetMapping("/login")
//    public String showLoginForm(Model model, HttpServletRequest request, HttpSession session) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userId".equals(cookie.getName())) {
//                    try {
//                        int userId = Integer.parseInt(cookie.getValue());
//                        Optional<User> userOptional = userRepository.findById(userId);
//
//                        if (userOptional.isPresent()) {
//                            User user = userOptional.get();
//                            session.setAttribute("userName", user.getFullname());
//                            session.setAttribute("taikhoanid", user.getId());
//                            session.setAttribute("loggedInUserEmail", user.getEmail());
//                            session.setAttribute("isAdmin", user.getAdmin());
//
//                            return user.getAdmin() ? "redirect:/quanly" : "redirect:/trangChu/form";
//                        }
//                    } catch (NumberFormatException e) {
//                        System.out.println("Lỗi chuyển đổi userId từ cookie: " + e.getMessage());
//                    }
//                }
//            }
//        }
//        return "nguoiDung/login";
//    }
//
//    // Giao diện HTML: Xử lý đăng nhập qua form
//    @PostMapping("/login")
//    public String loginUser(
//            @RequestParam String email,
//            @RequestParam String password,
//            @RequestParam(required = false) String rememberMe,
//            Model model,
//            HttpSession session,
//            HttpServletResponse response) {
//
//        Optional<User> userOptional = userRepository.findByEmail(email);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            if (user.getPassword().equals(password)) {
//                if (!user.getActivated()) {
//                    model.addAttribute("errorMessage", "Tài khoản của bạn bị cấm bởi ADMIN!");
//                    return "nguoiDung/login";
//                }
//                session.setAttribute("userName", user.getFullname());
//                session.setAttribute("taikhoanid", user.getId());
//                session.setAttribute("loggedInUserEmail", user.getEmail());
//                session.setAttribute("isAdmin", user.getAdmin());
//
//                if ("on".equals(rememberMe)) {
//                    Cookie userCookie = new Cookie("userId", String.valueOf(user.getId()));
//                    userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
//                    userCookie.setPath("/");
//                    response.addCookie(userCookie);
//                }
//
//                return user.getAdmin() ? "redirect:/quanly" : "redirect:/trangChu/form";
//            } else {
//                model.addAttribute("errorMessage", "Email không tồn tại hoặc Mật Khẩu Không chính xác!");
//            }
//        } else {
//            model.addAttribute("errorMessage", "Email không tồn tại hoặc Mật Khẩu Không chính xác!");
//        }
//
//        return "nguoiDung/login";
//    }
//
//    // Giao diện HTML: Đăng xuất
//    @GetMapping("/logout")
//    public String logout(HttpSession session, HttpServletResponse response) {
//        session.invalidate();
//
//        Cookie userCookie = new Cookie("userId", "");
//        userCookie.setMaxAge(0);
//        userCookie.setPath("/");
//        response.addCookie(userCookie);
//
//        return "redirect:/auth/login";
//    }
//}
//package org.example.java5.controllers.nguoiDung;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.example.java5.dto.LoginResponse;
//import org.example.java5.entity.User;
//import org.example.java5.jwt.JwtUtil;
//import org.example.java5.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/auth/api")
//public class dangNhap {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @GetMapping("/check-login")
//    public ResponseEntity<?> checkLogin(HttpServletRequest request, HttpSession session) {
//        Cookie[] cookies = request.getCookies();
//        Map<String, Object> response = new HashMap<>();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userId".equals(cookie.getName())) {
//                    try {
//                        int userId = Integer.parseInt(cookie.getValue());
//                        Optional<User> userOptional = userRepository.findById(userId);
//                        if (userOptional.isPresent()) {
//                            User user = userOptional.get();
//
//                            session.setAttribute("userName", user.getFullname());
//                            session.setAttribute("taikhoanid", user.getId());
//                            session.setAttribute("loggedInUserEmail", user.getEmail());
//                            session.setAttribute("isAdmin", user.getAdmin());
//
//                            LoginResponse loginResponse = new LoginResponse(
//                                    user.getId(), user.getFullname(), user.getEmail(), user.getAdmin()
//                            );
//
//                            response.put("status", "success");
//                            response.put("user", loginResponse);
//                            return ResponseEntity.ok(response);
//                        }
//                    } catch (NumberFormatException e) {
//                        response.put("status", "fail");
//                        response.put("message", "Cookie userId không hợp lệ");
//                        return ResponseEntity.badRequest().body(response);
//                    }
//                }
//            }
//        }
//
//        response.put("status", "fail");
//        response.put("message", "Chưa đăng nhập");
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//    }
//
//    @PostMapping(value = "/login", consumes = "application/json")
//    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody) {
//        String email = requestBody.get("email");
//        String password = requestBody.get("password");
//        Map<String, Object> result = new HashMap<>();
//
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isEmpty()) {
//            result.put("message", "Email hoặc mật khẩu không chính xác");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
//        }
//
//        User user = userOptional.get();
//
//        if (!user.getPassword().equals(password)) {
//            result.put("message", "Email hoặc mật khẩu không chính xác");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
//        }
//
//        if (!user.getActivated()) {
//            result.put("message", "Tài khoản bị cấm bởi ADMIN");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
//        }
//
//        // Generate JWT Token
//        String jwt = jwtUtil.generateToken(user.getEmail(),user.getAdmin());
//
//        // Create LoginResponse DTO
//        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getFullname(), user.getEmail(), user.getAdmin());
//
//        // Prepare result
//        result.put("token", jwt);
//        result.put("user", loginResponse);
//        result.put("userId", user.getId()); // 👈 thêm dòng này nếu bạn muốn tiện truy xuất
//        result.put("isAdmin", user.getAdmin());
//
//        return ResponseEntity.ok(result);
//    }
//
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpSession session, HttpServletResponse response) {
//        session.invalidate();
//
//        Cookie userCookie = new Cookie("userId", "");
//        userCookie.setMaxAge(0);
//        userCookie.setPath("/");
//        response.addCookie(userCookie);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("message", "Đăng xuất thành công");
//        return ResponseEntity.ok(result);
//    }
//}
//

package org.example.java5.controllers.nguoiDung;

import org.example.java5.dto.LoginResponse;
import org.example.java5.entity.User;
import org.example.java5.jwt.JwtUtil;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/api")
public class dangNhap {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/check-login")
    public ResponseEntity<?> checkLogin(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = jwtUtil.extractEmail(token);
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                LoginResponse loginResponse = new LoginResponse(user.getId(), user.getFullname(), user.getEmail(), user.getAdmin());

                response.put("status", "success");
                response.put("user", loginResponse);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Token không hợp lệ hoặc hết hạn");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("status", "fail");
        response.put("message", "Chưa đăng nhập hoặc token không hợp lệ");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String password = requestBody.get("password");

        Map<String, Object> result = new HashMap<>();

        // Kiểm tra người dùng tồn tại
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            result.put("message", "Email hoặc mật khẩu không chính xác");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        User user = userOptional.get();

        // Kiểm tra mật khẩu
        if (!user.getPassword().equals(password)) {
            result.put("message", "Email hoặc mật khẩu không chính xác");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        // Kiểm tra tài khoản có bị vô hiệu hóa không
        if (!user.getActivated()) {
            result.put("message", "Tài khoản bị cấm bởi ADMIN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }

        // Tạo JWT Token
        String jwt = jwtUtil.generateToken(user.getEmail(), user.getAdmin());

        // Tạo LoginResponse DTO
        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getFullname(), user.getEmail(), user.getAdmin());

        // Chuẩn bị kết quả trả về
        result.put("token", jwt);
        result.put("user", loginResponse);
        result.put("userId", user.getId());
        result.put("isAdmin", user.getAdmin());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok(result);
    }
}
