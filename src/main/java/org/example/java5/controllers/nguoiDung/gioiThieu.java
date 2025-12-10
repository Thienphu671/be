package org.example.java5.controllers.nguoiDung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class gioiThieu {
    @GetMapping ("gioiThieu/form")
    public String gioiThieu() {
        return "nguoiDung/gioiThieu";
    }

}
