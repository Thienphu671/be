package org.example.java5.controllers.nguoiDung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class thanhToan {
    @GetMapping("thanhToan/form")
    public String thanhToan() {
        return "nguoiDung/thanhToan";
    }
}
