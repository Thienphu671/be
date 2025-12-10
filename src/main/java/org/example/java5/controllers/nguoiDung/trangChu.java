package org.example.java5.controllers.nguoiDung;

import org.example.java5.Service.SanPhamService;
import org.example.java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class trangChu {

    @Autowired
    private SanPhamService sanPhamService; // Inject service

    @GetMapping("trangChu/form")
    public String home(Model model) {
        List<Product> products = sanPhamService.getAll(); // Lấy danh sách sản phẩm từ database
        model.addAttribute("products", products);
        return "nguoiDung/trangChu"; // Trả về view trang chủ
    }
}
