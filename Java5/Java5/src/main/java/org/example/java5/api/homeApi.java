package org.example.java5.api;

import org.example.java5.Service.SanPhamService;
import org.example.java5.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // <- Trả về JSON thay vì trả về view
@RequestMapping("/api/san-pham") // Prefix API
@CrossOrigin(origins = "*") // ✅ Cho phép gọi API từ bất kỳ domain nào (có thể là Vue, React, Postman, v.v.)
public class homeApi {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = sanPhamService.getAll();
        return ResponseEntity.ok(list);
    }


}
