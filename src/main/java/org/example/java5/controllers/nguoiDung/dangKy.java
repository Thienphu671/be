////
////
////
////package org.example.java5.controllers.nguoiDung;
////
////import org.example.java5.beans.UserRegistrationBean;
////import org.example.java5.entity.User;
////import org.example.java5.repository.UserRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import jakarta.validation.Valid;
////import java.io.File;
////import java.io.IOException;
////import java.nio.file.Files;
////import java.nio.file.Path;
////import java.nio.file.Paths;
////import java.util.UUID;
////
////@RestController
////@RequestMapping("/auth/api") // Đổi thành "/auth/api"
////public class dangKy {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    private static final String UPLOAD_DIR = "D:/Java5/uploads/";
////    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
////
////    // Đăng ký tài khoản qua JSON
////    @PostMapping("/DangKy") // Chuyển sang "/auth/api/DangKy"
////    public ResponseEntity<?> registerUser(
////            @Valid @RequestBody UserRegistrationBean userRegistrationBean,
////            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {
////
////        String errorMessage = validateAndSaveUser(userRegistrationBean, photoFile);
////        if (errorMessage != null) {
////            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
////        }
////
////        return ResponseEntity.ok(new SuccessResponse("Đăng ký tài khoản thành công!"));
////    }
////
////    // Logic chung để kiểm tra và lưu user
////    private String validateAndSaveUser(UserRegistrationBean userRegistrationBean, MultipartFile photoFile) {
////        // Kiểm tra email đã tồn tại
////        if (userRepository.findByEmail(userRegistrationBean.getEmail()).isPresent()) {
////            return "Email đã được sử dụng!";
////        }
////
////        // Kiểm tra số điện thoại đã tồn tại
////        if (userRepository.existsByPhoneNumber(userRegistrationBean.getPhoneNumber())) {
////            return "Số điện thoại đã được sử dụng!";
////        }
////
////        String fileName = "";
////        if (photoFile != null && !photoFile.isEmpty()) {
////            if (photoFile.getSize() > MAX_FILE_SIZE) {
////                return "Ảnh quá lớn! Chỉ chấp nhận file dưới 20MB.";
////            }
////            try {
////                File uploadDir = new File(UPLOAD_DIR);
////                if (!uploadDir.exists()) {
////                    Files.createDirectories(uploadDir.toPath());
////                }
////                fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
////                Path filePath = Paths.get(UPLOAD_DIR, fileName);
////                photoFile.transferTo(filePath.toFile());
////            } catch (IOException e) {
////                return "Lỗi khi lưu ảnh: " + e.getMessage();
////            }
////        }
////
////        try {
////            User user = new User();
////            user.setEmail(userRegistrationBean.getEmail());
////            user.setPassword(userRegistrationBean.getPassword());
////            user.setFullname(userRegistrationBean.getFullname());
////            user.setPhoneNumber(userRegistrationBean.getPhoneNumber());
////            user.setPhoto(fileName);
////            user.setActivated(true);
////            user.setAdmin(false);
////
////            userRepository.save(user);
////            return null; // Không có lỗi
////        } catch (Exception e) {
////            return "Đã có lỗi khi đăng ký tài khoản: " + e.getMessage();
////        }
////    }
////
////    // Lớp trả về lỗi
////    public static class ErrorResponse {
////        private String message;
////
////        public ErrorResponse(String message) {
////            this.message = message;
////        }
////
////        public String getMessage() {
////            return message;
////        }
////
////        public void setMessage(String message) {
////            this.message = message;
////        }
////    }
////
////    // Lớp trả về thành công
////    public static class SuccessResponse {
////        private String message;
////
////        public SuccessResponse(String message) {
////            this.message = message;
////        }
////
////        public String getMessage() {
////            return message;
////        }
////
////        public void setMessage(String message) {
////            this.message = message;
////        }
////    }
////}
//
//package org.example.java5.controllers.nguoiDung;
//
//import org.example.java5.beans.UserRegistrationBean;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.validation.Valid;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/auth/api")
//public class dangKy {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private static final String UPLOAD_DIR = "D:/Java5/uploads/";
//    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
//
//    // Đăng ký tài khoản qua multipart/form-data
//    @PostMapping("/DangKy")
//    public ResponseEntity<?> registerUser(
//            @Valid @ModelAttribute UserRegistrationBean userRegistrationBean,  // Dùng @ModelAttribute để nhận các trường không phải file
//            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {  // Dùng @RequestParam để nhận file
//
//        String errorMessage = validateAndSaveUser(userRegistrationBean, photoFile);
//        if (errorMessage != null) {
//            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
//        }
//
//        return ResponseEntity.ok(new SuccessResponse("Đăng ký tài khoản thành công!"));
//    }
//
//    // Logic chung để kiểm tra và lưu user
//    private String validateAndSaveUser(UserRegistrationBean userRegistrationBean, MultipartFile photoFile) {
//        // Kiểm tra email đã tồn tại
//        if (userRepository.findByEmail(userRegistrationBean.getEmail()).isPresent()) {
//            return "Email đã được sử dụng!";
//        }
//
//        // Kiểm tra số điện thoại đã tồn tại
//        if (userRepository.existsByPhoneNumber(userRegistrationBean.getPhoneNumber())) {
//            return "Số điện thoại đã được sử dụng!";
//        }
//
//        String fileName = "";
//        if (photoFile != null && !photoFile.isEmpty()) {
//            if (photoFile.getSize() > MAX_FILE_SIZE) {
//                return "Ảnh quá lớn! Chỉ chấp nhận file dưới 20MB.";
//            }
//            try {
//                File uploadDir = new File(UPLOAD_DIR);
//                if (!uploadDir.exists()) {
//                    Files.createDirectories(uploadDir.toPath());
//                }
//                fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
//                Path filePath = Paths.get(UPLOAD_DIR, fileName);
//                photoFile.transferTo(filePath.toFile());
//            } catch (IOException e) {
//                return "Lỗi khi lưu ảnh: " + e.getMessage();
//            }
//        }
//
//        try {
//            User user = new User();
//            user.setEmail(userRegistrationBean.getEmail());
//            user.setPassword(userRegistrationBean.getPassword());
//            user.setFullname(userRegistrationBean.getFullname());
//            user.setPhoneNumber(userRegistrationBean.getPhoneNumber());
//            user.setPhoto(fileName);
//            user.setActivated(true);
//            user.setAdmin(false);
//
//            userRepository.save(user);
//            return null; // Không có lỗi
//        } catch (Exception e) {
//            return "Đã có lỗi khi đăng ký tài khoản: " + e.getMessage();
//        }
//    }
//
//    // Lớp trả về lỗi
//    public static class ErrorResponse {
//        private String message;
//
//        public ErrorResponse(String message) {
//            this.message = message;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//
//    // Lớp trả về thành công
//    public static class SuccessResponse {
//        private String message;
//
//        public SuccessResponse(String message) {
//            this.message = message;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//}

package org.example.java5.controllers.nguoiDung;

import org.example.java5.beans.UserRegistrationBean;
import org.example.java5.entity.User;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/auth/api")
public class dangKy {

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "D:/Java5/uploads/";
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB

    // Đăng ký tài khoản qua multipart/form-data
    @PostMapping("/DangKy")
    public ResponseEntity<?> registerUser(
            @Valid @ModelAttribute UserRegistrationBean userRegistrationBean, // Dùng @ModelAttribute để nhận các trường không phải file
            BindingResult result, // BindingResult dùng để kiểm tra lỗi validation
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) { // Dùng @RequestParam để nhận file

        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessages.toString()));
        }

        String errorMessage = validateAndSaveUser(userRegistrationBean, photoFile);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }

        return ResponseEntity.ok(new SuccessResponse("Đăng ký tài khoản thành công!"));
    }

    // Logic chung để kiểm tra và lưu user
    private String validateAndSaveUser(UserRegistrationBean userRegistrationBean, MultipartFile photoFile) {
        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(userRegistrationBean.getEmail()).isPresent()) {
            return "Email đã được sử dụng!";
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (userRepository.existsByPhoneNumber(userRegistrationBean.getPhoneNumber())) {
            return "Số điện thoại đã được sử dụng!";
        }

        String fileName = "";
        if (photoFile != null && !photoFile.isEmpty()) {
            if (photoFile.getSize() > MAX_FILE_SIZE) {
                return "Ảnh quá lớn! Chỉ chấp nhận file dưới 20MB.";
            }
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    Files.createDirectories(uploadDir.toPath());
                }
                fileName = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                photoFile.transferTo(filePath.toFile());
            } catch (IOException e) {
                return "Lỗi khi lưu ảnh: " + e.getMessage();
            }
        }

        try {
            User user = new User();
            user.setEmail(userRegistrationBean.getEmail());
            user.setPassword(userRegistrationBean.getPassword());
            user.setFullname(userRegistrationBean.getFullname());
            user.setPhoneNumber(userRegistrationBean.getPhoneNumber());
            user.setPhoto(fileName);
            user.setActivated(true);
            user.setAdmin(false);

            userRepository.save(user);
            return null; // Không có lỗi
        } catch (Exception e) {
            return "Đã có lỗi khi đăng ký tài khoản: " + e.getMessage();
        }
    }

    // Lớp trả về lỗi
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Lớp trả về thành công
    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
