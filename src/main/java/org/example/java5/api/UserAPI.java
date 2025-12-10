package org.example.java5.api;

import org.example.java5.Service.UserService;
import org.example.java5.dto.UserDTO;
import org.example.java5.dto.UserResponse;
import org.example.java5.entity.User;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserAPI {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // Chuyển đổi User -> UserDTO
    private UserResponse convertToDTO(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullname(),
                user.getPhoneNumber(),
                user.getPhoto(),
                user.getActivated(),
                user.getAdmin()
        );
    }

    // Lấy danh sách người dùng (sử dụng DTO)
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers(
            @RequestParam(required = false) String fullname,
            @RequestParam(required = false) String email) {

        List<User> users;

        if (fullname != null && !fullname.isEmpty()) {
            users = userRepository.findByFullnameContainingAndAdminFalse(fullname);
        } else if (email != null && !email.isEmpty()) {
            users = userRepository.findByEmailContainingAndAdminFalse(email);
        } else {
            users = userRepository.findByAdminFalse();
        }

        // Chuyển đổi danh sách Entity sang DTO
        List<UserResponse> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }


    // Lấy thông tin người dùng theo ID (sử dụng DTO)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Xóa người dùng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID người dùng không hợp lệ");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("Xóa người dùng thành công");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/toggleActive/{id}")
    public ResponseEntity<?> toggleUserActivation(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID người dùng không hợp lệ");
        }
        userService.toggleActive(id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    
    @PostMapping("/toggleAdmin/{id}")
    public ResponseEntity<?> toggleUserAdmin(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID người dùng không hợp lệ");
        }
        userService.toggleAdmin(id);
        User user = userService.getUserById(id); // Lấy lại dữ liệu sau khi cập nhật
        return ResponseEntity.ok(user);
    }

}
