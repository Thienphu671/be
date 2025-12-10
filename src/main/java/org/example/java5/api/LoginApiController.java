////package org.example.java5.api;
////
////import jakarta.servlet.http.Cookie;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import jakarta.servlet.http.HttpSession;
////import org.example.java5.dto.LoginResponse;
////import org.example.java5.entity.User;
////import org.example.java5.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.HashMap;
////import java.util.Map;
////import java.util.Optional;
////
////@RestController
////@RequestMapping("/auth/api")
////@CrossOrigin(origins = "http://localhost:3006", allowCredentials = "true")
////public class LoginApiController {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    // API: Kiểm tra đăng nhập qua cookie
////    @GetMapping("/check-login")
////    public ResponseEntity<Map<String, Object>> checkLogin(HttpServletRequest request, HttpSession session) {
////        Map<String, Object> response = new HashMap<>();
////        Cookie[] cookies = request.getCookies();
////
////        if (cookies != null) {
////            for (Cookie cookie : cookies) {
////                if ("userId".equals(cookie.getName())) {
////                    try {
////                        int userId = Integer.parseInt(cookie.getValue());
////                        Optional<User> userOptional = userRepository.findById(userId);
////
////                        if (userOptional.isPresent()) {
////                            User user = userOptional.get();
////                            session.setAttribute("userName", user.getFullname());
////                            session.setAttribute("taikhoanid", user.getId());
////                            session.setAttribute("loggedInUserEmail", user.getEmail());
////                            session.setAttribute("isAdmin", user.getAdmin());
////
////                            LoginResponse loginResponse = new LoginResponse(
////                                    user.getId(),
////                                    user.getFullname(),
////                                    user.getEmail(),
////                                    user.getAdmin(),
////                                    user.getAdmin() ? "/quanly" : "/trangChu/form"
////                            );
////
////                            response.put("status", "success");
////                            response.put("message", "Đăng nhập tự động thành công qua cookie");
////                            response.put("user", loginResponse);
////                            return ResponseEntity.ok(response);
////                        }
////                    } catch (NumberFormatException e) {
////                        response.put("status", "error");
////                        response.put("message", "Cookie userId không hợp lệ");
////                        return ResponseEntity.badRequest().body(response);
////                    }
////                }
////            }
////        }
////
////        response.put("status", "info");
////        response.put("message", "Không tìm thấy cookie hợp lệ, vui lòng đăng nhập");
////        return ResponseEntity.status(401).body(response);
////    }
////
////    // API: Đăng nhập qua JSON
////    @PostMapping("/login")
////    public ResponseEntity<Map<String, Object>> loginUserApi(
////            @RequestParam String email,
////            @RequestParam String password,
////            @RequestParam(required = false) String rememberMe,
////            HttpSession session,
////            HttpServletResponse response) {
////
////        Map<String, Object> result = new HashMap<>();
////        Optional<User> userOptional = userRepository.findByEmail(email);
////
////        if (userOptional.isPresent()) {
////            User user = userOptional.get();
////
////            if (user.getPassword().equals(password)) {
////                if (!user.getActivated()) {
////                    result.put("status", "error");
////                    result.put("message", "Tài khoản của bạn bị cấm bởi ADMIN!");
////                    return ResponseEntity.status(403).body(result);
////                }
////
////                session.setAttribute("userName", user.getFullname());
////                session.setAttribute("taikhoanid", user.getId());
////                session.setAttribute("loggedInUserEmail", user.getEmail());
////                session.setAttribute("isAdmin", user.getAdmin());
////
////                if ("on".equals(rememberMe)) {
////                    Cookie userCookie = new Cookie("userId", String.valueOf(user.getId()));
////                    userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
////                    userCookie.setPath("/");
////                    response.addCookie(userCookie);
////                }
////
////                LoginResponse loginResponse = new LoginResponse(
////                        user.getId(),
////                        user.getFullname(),
////                        user.getEmail(),
////                        user.getAdmin(),
////                        user.getAdmin() ? "admin/quanly" : "/trangChu/form"
////                );
////
////                result.put("status", "success");
////                result.put("message", "Đăng nhập thành công");
////                result.put("user", loginResponse);
////                return ResponseEntity.ok(result);
////            } else {
////                result.put("status", "error");
////                result.put("message", "Email không tồn tại hoặc mật khẩu không chính xác!");
////                return ResponseEntity.badRequest().body(result);
////            }
////        } else {
////            result.put("status", "error");
////            result.put("message", "Email không tồn tại hoặc mật khẩu không chính xác!");
////            return ResponseEntity.badRequest().body(result);
////        }
////    }
////
////    // API: Đăng xuất qua JSON
////    @GetMapping("/logout")
////    public ResponseEntity<Map<String, Object>> logoutApi(HttpSession session, HttpServletResponse response) {
////        session.invalidate();
////
////        Cookie userCookie = new Cookie("userId", "");
////        userCookie.setMaxAge(0);
////        userCookie.setPath("/");
////        response.addCookie(userCookie);
////
////        Map<String, Object> result = new HashMap<>();
////        result.put("status", "success");
////        result.put("message", "Đăng xuất thành công");
////        result.put("redirect", "/auth/login");
////        return ResponseEntity.ok(result);
////    }
////}
//package org.example.java5.api;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.example.java5.dto.LoginResponse;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/auth/api")
//@CrossOrigin(origins = "http://localhost:3006", allowCredentials = "true")
//public class LoginApiController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Kiểm tra đăng nhập qua cookie
//    @GetMapping("/check-login")
//    public ResponseEntity<?> checkLogin(HttpServletRequest request, HttpSession session) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userId".equals(cookie.getName())) {
//                    try {
//                        int userId = Integer.parseInt(cookie.getValue());
//                        Optional<User> userOpt = userRepository.findById(userId);
//                        if (userOpt.isPresent()) {
//                            User user = userOpt.get();
//                            session.setAttribute("userName", user.getFullname());
//                            session.setAttribute("taikhoanid", user.getId());
//                            session.setAttribute("loggedInUserEmail", user.getEmail());
//                            session.setAttribute("isAdmin", user.getAdmin());
//
//                            return ResponseEntity.ok(Map.of(
//                                    "status", "success",
//                                    "message", "Đăng nhập tự động thành công",
//                                    "user", new LoginResponse(
//                                            user.getId(),
//                                            user.getFullname(),
//                                            user.getEmail(),
//                                            user.getAdmin(),
//                                            user.getAdmin() ? "/admin/quanly" : "/trangChu/form"
//                                    )
//                            ));
//                        }
//                    } catch (NumberFormatException ignored) {}
//                }
//            }
//        }
//        return ResponseEntity.status(401).body(Map.of("status", "info", "message", "Chưa đăng nhập"));
//    }
//
//    // Đăng nhập qua JSON body
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> payload,
//                                   HttpSession session,
//                                   HttpServletResponse response) {
//        String email = payload.get("email");
//        String password = payload.get("password");
//        boolean rememberMe = Boolean.parseBoolean(payload.getOrDefault("rememberMe", "false"));
//
//        Optional<User> userOpt = userRepository.findByEmail(email);
//        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
//            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Sai tài khoản hoặc mật khẩu"));
//        }
//
//        User user = userOpt.get();
//        if (!user.getActivated()) {
//            return ResponseEntity.status(403).body(Map.of("status", "error", "message", "Tài khoản đã bị khóa"));
//        }
//
//        session.setAttribute("userName", user.getFullname());
//        session.setAttribute("taikhoanid", user.getId());
//        session.setAttribute("loggedInUserEmail", user.getEmail());
//        session.setAttribute("isAdmin", user.getAdmin());
//
//        if (rememberMe) {
//            Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
//            cookie.setMaxAge(7 * 24 * 60 * 60);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//        }
//
//        return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "message", "Đăng nhập thành công",
//                "user", new LoginResponse(
//                        user.getId(),
//                        user.getFullname(),
//                        user.getEmail(),
//                        user.getAdmin(),
//                        user.getAdmin() ? "/admin/quanly" : "/trangChu/form"
//                )
//        ));
//    }
//
//    // Đăng xuất
//    @GetMapping("/logout")
//    public ResponseEntity<?> logout(HttpSession session, HttpServletResponse response) {
//        session.invalidate();
//        Cookie cookie = new Cookie("userId", "");
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "message", "Đăng xuất thành công"
//        ));
//    }
//}
