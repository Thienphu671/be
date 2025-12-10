package org.example.java5.Service;

import org.example.java5.entity.Category;
import org.example.java5.repository.CategoryReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryReponsitory danhMucRepository;

    // Lấy danh sách tất cả danh mục
    public List<Category> getAllCategories() {
        return danhMucRepository.findAll();
    }

    // Tìm kiếm danh mục theo từ khóa (không phân biệt hoa thường)
    public List<Category> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return danhMucRepository.findAll();
        }
        return danhMucRepository.findByName("%" + keyword.trim() + "%"); // Tìm kiếm linh hoạt hơn
    }

    // Lấy danh mục theo ID
    public Optional<Category> getCategoryById(int id) {
        return danhMucRepository.findById(id);
    }
    public Optional<Category> getById(int id) {
        return danhMucRepository.findById(id);
    }

    // Thêm hoặc cập nhật danh mục
    public void saveOrUpdateCategory(Category category) {
        danhMucRepository.save(category);
    }

    // Xóa danh mục theo ID (đảm bảo toàn vẹn dữ liệu)
    @Transactional
    public boolean deleteCategory(int id) {
        if (danhMucRepository.existsById(id)) {
            danhMucRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<Category> getAll() {
        return danhMucRepository.findAll();
    }

    public void toggleCategoryStatus(Integer id) {
        danhMucRepository.findById(id).ifPresentOrElse(product -> {
            int newStatus = product.getStatus() == 0 ? 1 : 0; // Đảo trạng thái
            product.setStatus(newStatus);
            danhMucRepository.save(product);
        }, () -> {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        });
    }

}
