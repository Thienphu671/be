package org.example.java5.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.java5.Service.CategoryService;
import org.example.java5.dto.danhMucResponse;
import org.example.java5.entity.Category;
import org.example.java5.entity.User;
import org.example.java5.jwt.JwtUtil;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/categories")
public class DanhMucAPI {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "keyword", required = false) String keyword) {
        List<danhMucResponse> categories = categoryService.searchCategories(keyword)
                .stream()
                .map(cat -> new danhMucResponse(cat.getId(), cat.getTen(), cat.getStatus()))
                .collect(Collectors.toList());

        if (categories.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "Không có danh mục nào.", "categories", categories));
        }
        return ResponseEntity.ok(Map.of("message", "Lấy danh sách danh mục thành công!", "categories", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(cat -> ResponseEntity.ok(Map.of(
                        "message", "Lấy danh mục thành công!",
                        "category", new danhMucResponse(cat.getId(), cat.getTen(), cat.getStatus()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Không tìm thấy danh mục với ID: " + id)));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCategory(
            @RequestParam("ten") String ten,
            @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        Category category = new Category();
        category.setTen(ten);
        category.setStatus(status);

        // TODO: Xử lý lưu file (nếu cần)
        categoryService.saveOrUpdateCategory(category);
        Optional<Category> savedCategory = categoryService.getCategoryById(category.getId());

        return savedCategory.map(cat -> ResponseEntity.ok(Map.of(
                        "message", "Thêm danh mục thành công!",
                        "category", new danhMucResponse(cat.getId(), cat.getTen(), cat.getStatus()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Thêm danh mục thất bại!")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable int id,
            @RequestParam("ten") String ten,
            @RequestParam(value = "status", required = false, defaultValue = "1") Integer status,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (!categoryOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Không tìm thấy danh mục để cập nhật."));
        }

        Category category = categoryOpt.get();
        category.setTen(ten);
        category.setStatus(status);

        // TODO: Xử lý lưu file (nếu cần)

        categoryService.saveOrUpdateCategory(category);
        Optional<Category> updatedCategory = categoryService.getCategoryById(id);

        return updatedCategory.map(cat -> ResponseEntity.ok(Map.of(
                        "message", "Cập nhật danh mục thành công!",
                        "category", new danhMucResponse(cat.getId(), cat.getTen(), cat.getStatus()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Cập nhật danh mục thất bại!")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.ok(Map.of("message", "Xóa danh mục thành công!"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Không tìm thấy danh mục để xóa."));
    }


    @PutMapping("/toggle-status/{id}")
    @ResponseBody
    public ResponseEntity<?> toggleStatus(@PathVariable Integer id) {
        try {
            categoryService.toggleCategoryStatus(id);
            return ResponseEntity.ok().body("Status toggled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error toggling status: " + e.getMessage());
        }
    }

}