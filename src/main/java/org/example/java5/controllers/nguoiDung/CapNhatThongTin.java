//package org.example.java5.controllers.nguoiDung;
//
//import jakarta.servlet.http.HttpSession;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Optional;
//import java.util.logging.Logger;
//
//@Controller
//@RequestMapping("/thayDoiThongTin")
//public class CapNhatThongTin {
//    private final UserRepository userRepository;
//    private static final Logger logger = Logger.getLogger(CapNhatThongTin.class.getName());
//    private static final String UPLOAD_DIR = "D:/Java5/uploads/";
//
//    public CapNhatThongTin(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping
//    public String hienThiForm(HttpSession session, Model model) {
//        String email = (String) session.getAttribute("loggedInUserEmail");
//        if (email == null) {
//            return "redirect:/auth/login";
//        }
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isPresent()) {
//            model.addAttribute("user", optionalUser.get());
//            return "nguoiDung/thayDoiThongTin";
//        }
//        return "redirect:/auth/login";
//    }
//
//    @PostMapping
//    public String capNhatThongTin(
//            @RequestParam(value = "photo", required = false) MultipartFile photo,
//            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "fullname", required = false) String fullname,
//            @RequestParam(value = "phone", required = false) String phone,
//            HttpSession session,
//            Model model) {
//        String loggedInEmail = (String) session.getAttribute("loggedInUserEmail");
//        if (loggedInEmail == null) {
//            return "redirect:/login";
//        }
//
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        // Kiểm tra email
//        if (email == null || email.isEmpty()) {
//            model.addAttribute("error", "Email không được để trống!");
//            model.addAttribute("user", user);
//            logger.info("Email error: Email không được để trống!");
//            return "nguoiDung/thayDoiThongTin";
//        }
//        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
//            model.addAttribute("error", "Email không đúng định dạng!");
//            model.addAttribute("user", user);
//            logger.info("Email error: Email không hợp lệ - " + email);
//            return "nguoiDung/thayDoiThongTin";
//        }
//        if (userRepository.existsByEmail(email) && !email.equals(user.getEmail())) {
//            model.addAttribute("error", "Email đã được sử dụng!");
//            model.addAttribute("user", user);
//            logger.info("Email error: Email đã được sử dụng - " + email);
//            return "nguoiDung/thayDoiThongTin";
//        }
//        user.setEmail(email);
//
//        // Kiểm tra fullname (bắt buộc không được để trống)
//        if (fullname == null || fullname.trim().isEmpty()) {
//            model.addAttribute("error", "Họ tên không được để trống!");
//            model.addAttribute("user", user);
//            logger.info("Fullname error: Họ tên không được để trống!");
//            return "nguoiDung/thayDoiThongTin";
//        }
//        if (fullname.length() < 2 || fullname.length() > 50) {
//            model.addAttribute("error", "Họ tên phải từ 2 đến 50 ký tự!");
//            model.addAttribute("user", user);
//            logger.info("Fullname error: Họ tên không hợp lệ - " + fullname);
//            return "nguoiDung/thayDoiThongTin";
//        }
//        user.setFullname(fullname);
//
//        // Kiểm tra số điện thoại
//        if (phone != null && !phone.isEmpty()) {
//            if (!phone.matches("\\d{10,15}")) {
//                model.addAttribute("phoneError", "Số điện thoại phải từ 10 đến 15 chữ số!");
//                model.addAttribute("user", user);
//                logger.info("Phone error: Số điện thoại không hợp lệ - " + phone);
//                return "nguoiDung/thayDoiThongTin";
//            }
//            if (userRepository.existsByPhoneNumber(phone) && !phone.equals(user.getPhoneNumber())) {
//                model.addAttribute("phoneError", "Số điện thoại đã được sử dụng!");
//                model.addAttribute("user", user);
//                logger.info("Phone error: Số điện thoại đã được sử dụng - " + phone);
//                return "nguoiDung/thayDoiThongTin";
//            }
//            user.setPhoneNumber(phone);
//        }
//
//        // Kiểm tra và xử lý ảnh
//        if (photo != null && !photo.isEmpty()) {
//            try {
//                String originalFilename = photo.getOriginalFilename();
//                if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
//                    model.addAttribute("error", "Định dạng file không hợp lệ!");
//                    model.addAttribute("user", user);
//                    return "nguoiDung/thayDoiThongTin";
//                }
//                File uploadFolder = new File(UPLOAD_DIR);
//                if (!uploadFolder.exists()) {
//                    uploadFolder.mkdirs();
//                }
//                String fileName = java.util.UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
//                File destinationFile = new File(UPLOAD_DIR + fileName);
//                photo.transferTo(destinationFile);
//                user.setPhoto(fileName);
//            } catch (IOException e) {
//                model.addAttribute("error", "Lỗi khi tải file lên!");
//                model.addAttribute("user", user);
//                logger.info("Photo error: Lỗi khi tải file lên - " + e.getMessage());
//                return "nguoiDung/thayDoiThongTin";
//            }
//        }
//
//        // Lưu thông tin
//        try {
//            userRepository.save(user);
//            session.setAttribute("loggedInUserEmail", user.getEmail());
//            session.setAttribute("user", user);
//            model.addAttribute("successMessage", "Cập nhật thông tin thành công!");
//        } catch (Exception e) {
//            model.addAttribute("error", "Lỗi khi lưu vào cơ sở dữ liệu!");
//            model.addAttribute("user", user);
//            logger.info("Database error: Lỗi khi lưu - " + e.getMessage());
//            return "nguoiDung/thayDoiThongTin";
//        }
//
//        return "redirect:/thongtin";
//    }
//}










package org.example.java5.controllers.nguoiDung;

import org.example.java5.dto.ResponseDTO;
import org.example.java5.dto.UserDTO;
import org.example.java5.entity.User;
import org.example.java5.repository.UserRepository;
import org.example.java5.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/thayDoiThongTin")
public class CapNhatThongTin {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;  // Inject JwtUtil
    private static final Logger logger = Logger.getLogger(CapNhatThongTin.class.getName());
    private static final String UPLOAD_DIR = "D:/Java5/uploads/";

    public CapNhatThongTin(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> capNhatThongTin(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "phone", required = false) String phone) {

        // Xác thực JWT token
        String loggedInEmail = jwtUtil.extractEmail(token);
        if (loggedInEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("Token không hợp lệ.", false));
        }

        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO("Người dùng không tồn tại.", false));
        }

        User user = optionalUser.get();

        // Kiểm tra email
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Email không được để trống!", false));
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Email không đúng định dạng!", false));
        }
        if (userRepository.existsByEmail(email) && !email.equals(user.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Email đã được sử dụng!", false));
        }
        user.setEmail(email);

        // Kiểm tra fullname
        if (fullname == null || fullname.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Họ tên không được để trống!", false));
        }
        if (fullname.length() < 2 || fullname.length() > 50) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Họ tên phải từ 2 đến 50 ký tự!", false));
        }
        user.setFullname(fullname);

        // Kiểm tra số điện thoại
        if (phone != null && !phone.isEmpty()) {
            if (!phone.matches("\\d{10,15}")) {
                return ResponseEntity.badRequest().body(new ResponseDTO("Số điện thoại phải từ 10 đến 15 chữ số!", false));
            }
            if (userRepository.existsByPhoneNumber(phone) && !phone.equals(user.getPhoneNumber())) {
                return ResponseEntity.badRequest().body(new ResponseDTO("Số điện thoại đã được sử dụng!", false));
            }
            user.setPhoneNumber(phone);
        }

        // Kiểm tra và xử lý ảnh
        if (photo != null && !photo.isEmpty()) {
            try {
                String originalFilename = photo.getOriginalFilename();
                if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    return ResponseEntity.badRequest().body(new ResponseDTO("Định dạng file không hợp lệ!", false));
                }
                File uploadFolder = new File(UPLOAD_DIR);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }
                String fileName = java.util.UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
                File destinationFile = new File(UPLOAD_DIR + fileName);
                photo.transferTo(destinationFile);
                user.setPhoto(fileName);
            } catch (IOException e) {
                logger.info("Photo error: Lỗi khi tải file lên - " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO("Lỗi khi tải file lên!", false));
            }
        }

        // Lưu thông tin
        try {
            userRepository.save(user);

            // Trả về thông tin người dùng
            UserDTO userDTO = new UserDTO(user.getEmail(), user.getFullname(), user.getPhoneNumber(), user.getPhoto());
            return ResponseEntity.ok(new ResponseDTO("Cập nhật thông tin thành công!", true, userDTO));
        } catch (Exception e) {
            logger.info("Database error: Lỗi khi lưu - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("Lỗi khi lưu vào cơ sở dữ liệu!", false));
        }
    }
}

