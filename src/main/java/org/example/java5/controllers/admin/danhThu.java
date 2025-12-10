package org.example.java5.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class danhThu {
    @GetMapping("danhThu/form")
    public String danhThu() {
        return "admin/danhThu";
    }
}
