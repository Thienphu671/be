package org.example.java5.controllers.admin;

import org.example.java5.Service.CategoryService;
import org.example.java5.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class danhMuc {

    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách danh mục
    @GetMapping
    public String listCategories(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("categories", categoryService.searchCategories(keyword));
        model.addAttribute("keyword", keyword); // Giữ lại giá trị đã nhập
        return "admin/category-list.html";
    }

    // Hiển thị form thêm danh mục
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form.html";
    }

    // Lưu danh mục mới hoặc cập nhật danh mục
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.saveOrUpdateCategory(category);
        return "redirect:/categories";
    }

    // Hiển thị form chỉnh sửa danh mục
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        category.ifPresent(value -> model.addAttribute("category", value));
        return "admin/category-form.html";
    }

    // Xóa danh mục
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        if (categoryService.deleteCategory(id)) {
            return "redirect:/categories";  // Xóa thành công
        }
        return "redirect:/categories?error=notfound"; // Nếu ID không tồn tại
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Integer id) {
        categoryService.toggleCategoryStatus(id);
        return "redirect:/categories";
    }
}
