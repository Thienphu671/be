package org.example.java5.controllers.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class main {
    @GetMapping("/quanly")
    public String adminPage(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");

        if (isAdmin == null || !isAdmin) {
            return "redirect:/trangChu/form"; // Redirect nếu không phải admin
        }

        return "admin/navbar"; // Trả về trang admin nếu hợp lệ
    }
}
