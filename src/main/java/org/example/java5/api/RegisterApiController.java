//package org.example.java5.api;
//
//import org.example.java5.beans.UserRegistrationBean;
//import org.example.java5.dto.RegisterResponse;
//import org.example.java5.entity.User;
//import org.example.java5.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.validation.Valid;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/auth/api")
//public class RegisterApiController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private static final String UPLOAD_DIR = "D:/Java5/uploads/";
//    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
//
//    // API: Xử lý đăng ký qua JSON
//    @PostMapping(value = "/DangKy", consumes = {"multipart/form-data"})
//    public ResponseEntity<RegisterResponse> registerUserApi(
//            @Valid @ModelAttribute UserRegistrationBean userRegistrationBean,
//            BindingResult result,
//            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {
//
//        if (result.hasErrors()) {
//            Map<String, String> errors = new HashMap<>();
//            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
//            return ResponseEntity.badRequest()
//                    .body(new RegisterResponse("error", "Dữ liệu không hợp lệ: " + errors.toString()));
//        }
//
//        String errorMessage = validateAndSaveUser(userRegistrationBean, photoFile);
//        if (errorMessage != null) {
//            return ResponseEntity.badRequest()
//                    .body(new RegisterResponse("error", errorMessage));
//        }
//
//        User savedUser = userRepository.findByEmail(userRegistrationBean.getEmail()).get();
//        RegisterResponse.UserData userData = new RegisterResponse.UserData(
//                savedUser.getId(),
//                savedUser.getFullname(),
//                savedUser.getEmail(),
//                savedUser.getPhoneNumber()
//        );
//
//        return ResponseEntity.ok(
//                new RegisterResponse("success", "Đăng ký tài khoản thành công!", "/login", userData)
//        );
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
//    // Xử lý lỗi validation cho API
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<RegisterResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
//        return ResponseEntity.badRequest()
//                .body(new RegisterResponse("error", "Dữ liệu không hợp lệ: " + errors.toString()));
//    }
//}