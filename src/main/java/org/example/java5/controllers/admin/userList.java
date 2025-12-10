package org.example.java5.controllers.admin;

import org.example.java5.Service.UserService;
import org.example.java5.entity.User;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class userList {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("list/users")
    public String listUsers(@RequestParam(required = false) String fullname,
                            @RequestParam(required = false) String email,
                            Model model) {
        List<User> users;

        // Tìm kiếm theo fullname nếu có
        if (fullname != null && !fullname.isEmpty()) {
            users = userRepository.findByFullnameContaining(fullname);
        }
        // Tìm kiếm theo email nếu có
        else if (email != null && !email.isEmpty()) {
            users = userRepository.findByEmailContaining(email);
        }
        // Nếu không có tham số tìm kiếm, lấy tất cả user
        else {
            users = userRepository.findAll();
        }

        model.addAttribute("users", users);
        model.addAttribute("fullname", fullname); // Trả lại giá trị tìm kiếm để hiển thị trong form
        model.addAttribute("email", email); // Trả lại giá trị tìm kiếm để hiển thị trong form
        return "admin/userList"; // Trả về trang danh sách tài khoản
    }
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        // Delete the user by id
        userRepository.deleteById(id);
        return "redirect:/list/users"; // Redirect to the user list after deletion
    }
    @Autowired
    private UserService userService;

    @PostMapping("/list/users/toggleActive/{id}")
    public String toggleUserActivation(@PathVariable int id) {
        userService.toggleActive(id);
        return "redirect:/list/users"; // Chuyển hướng sau khi cập nhật
    }
    @PostMapping("/list/users/toggleAdmin/{id}")
    public String toggleUserAdmin(@PathVariable int id) {
        userService.toggleAdmin(id);
        return "redirect:/list/users"; // Chuyển hướng sau khi cập nhật
    }

}
