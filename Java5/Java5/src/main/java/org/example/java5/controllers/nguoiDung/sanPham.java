package org.example.java5.controllers.nguoiDung;

import jakarta.servlet.http.HttpSession;
import org.example.java5.Service.SanPhamService;
import org.example.java5.entity.Product;
import org.example.java5.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class sanPham {  // Đổi tên class đúng chuẩn PascalCase

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("taikhoanid"); // Lấy userId từ session
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Chưa đăng nhập"));
        }
        return ResponseEntity.ok(Map.of("userId", userId));
    }



    @GetMapping("/sanPham")
    public String hienThiSanPham(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "category", required = false) Integer category,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "8") int size,
                                 Model model) {
        // Tạo đối tượng Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách sản phẩm có phân trang
        Page<Product> productPage = sanPhamService.searchProductsPaged(keyword, category, pageable);

        // Chỉ lấy sản phẩm có trạng thái = 0
        List<Product> filteredProducts = productPage.getContent().stream()
                .filter(product -> product.getStatus() == 0)
                .toList();

        // Lấy danh mục sản phẩm
        List<Category> categories = sanPhamService.getAllCategories();
        categories = categories.stream()
                .filter(category1 -> category1.getStatus() == 0)
                .toList();

        // Sắp xếp sản phẩm theo giá (nếu có yêu cầu)
        if (sort != null && (sort.equalsIgnoreCase("asc") || sort.equalsIgnoreCase("desc"))) {
            boolean ascending = "asc".equalsIgnoreCase(sort);
            filteredProducts = sanPhamService.sortByPrice(filteredProducts, ascending);
        }

        // Đưa dữ liệu vào Model để hiển thị trên giao diện
        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("size", size);

        return "nguoiDung/sanPham";
    }
}
