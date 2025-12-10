//package org.example.java5.controllers.nguoiDung;
//
//import jakarta.servlet.http.HttpSession;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/thongtin")
//public class thongtin {
//    private final UserRepository userRepository;
//
//    public thongtin(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping
//    public String profile(HttpSession session, Model model) {
//        String email = (String) session.getAttribute("loggedInUserEmail");
//
//        if (email == null) {
//            return "redirect:/auth/login";
//        }
//
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            session.setAttribute("user", user);
//            model.addAttribute("user", user);
//        } else {
//            model.addAttribute("user", new User());
//        }
//
//        return "nguoiDung/thongtin";
//    }
//}

package org.example.java5.controllers.nguoiDung;

import jakarta.servlet.http.HttpServletRequest;
import org.example.java5.dto.UserProfileResponse;
import org.example.java5.entity.User;
import org.example.java5.jwt.JwtUtil;
import org.example.java5.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

@RestController
@RequestMapping("/api/thongtin")
public class thongtin {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public thongtin(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = jwtUtil.extractEmail(authHeader);
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("Không tìm thấy người dùng!");
            }

            User user = optionalUser.get();
            UserProfileResponse response = new UserProfileResponse(
                    user.getEmail(),
                    user.getFullname(),
                    user.getPhoneNumber(),
                    user.getPhoto()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token không hợp lệ hoặc hết hạn!");
        }
    }
}
