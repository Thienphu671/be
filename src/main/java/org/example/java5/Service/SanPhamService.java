package org.example.java5.Service;

import jakarta.transaction.Transactional;
import org.example.java5.dto.ProductDTO;
import org.example.java5.entity.Product;
import org.example.java5.entity.Category;
import org.example.java5.repository.CategoryReponsitory;
import org.example.java5.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // Đảm bảo tính nhất quán của dữ liệu
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private CategoryReponsitory categoryRepository; // Thêm repository để lấy danh mục

    //  Lấy tất cả sản phẩm
    public List<Product> getAll() {
        return sanPhamRepository.findAll();
    }

    //  Lấy sản phẩm theo ID
    public Optional<Product> getById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    //  Thêm sản phẩm mới
    public void save(Product product) {
        sanPhamRepository.save(product);
    }

    public void delete(Integer id) {
        if (sanPhamRepository.existsById(id)) {
            sanPhamRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm!");
        }
    }
    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    public void update(Product product) {
        sanPhamRepository.findById(product.getId()).ifPresentOrElse(existingProduct -> {
            // Kiểm tra nếu ảnh mới bị rỗng thì giữ nguyên ảnh cũ
            if (product.getHinh() == null || product.getHinh().isBlank()) {
                product.setHinh(existingProduct.getHinh());
            }

            // Cập nhật các trường khác
            existingProduct.setTen(product.getTen());
            existingProduct.setGia(product.getGia());
            existingProduct.setSoluong(product.getSoluong());
            existingProduct.setMota(formatMota(product.getMota()));
            existingProduct.setDanhMuc(product.getDanhMuc());

            existingProduct.setKichthuoc(product.getKichthuoc());
            existingProduct.setHinh(product.getHinh());

            // ⚠ Kiểm tra số lượng sản phẩm sau khi cập nhật
            if (product.getSoluong() == 0) {
                existingProduct.setStatus(1); // Đánh dấu là hết hàng
            }

            // Lưu sản phẩm đã cập nhật vào CSDL
            sanPhamRepository.save(existingProduct);
        }, () -> {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        });
    }

    //  Tìm kiếm sản phẩm theo tên (không phân biệt chữ hoa/thường)
    public List<Product> searchByName(String keyword) {
        return sanPhamRepository.findByTenContainingIgnoreCase(keyword);
    }

    //  Tìm kiếm sản phẩm theo danh mục (không phân biệt chữ hoa/thường)
    public List<Product> searchByCategory(String category) {
        return sanPhamRepository.findByDanhMuc_TenContainingIgnoreCase(category);
    }

    //  Tìm kiếm sản phẩm theo từ khóa, danh mục và loại
    public List<Product> searchProducts(String keyword, Integer category) {
        return sanPhamRepository.findByFilters(keyword, category);
    }

    //  Lấy danh sách tất cả danh mục
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //  Định dạng mô tả để xuống dòng sau 30 chữ
    private String formatMota(String mota) {
        if (mota == null || mota.isBlank()) {
            return "";
        }
        String[] words = mota.split(" ");
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            formatted.append(words[i]).append(" ");
            if ((i + 1) % 30 == 0) {
                formatted.append("\n");
            }
        }
        return formatted.toString().trim();
    }

    //giá tăng dần và giảm dần
    public List<Product> sortByPrice(List<Product> products, boolean ascending) {
        return products.stream()
                .sorted(ascending ? Comparator.comparing(Product::getGia)
                        : Comparator.comparing(Product::getGia).reversed())
                .collect(Collectors.toList());

        //chuyển products thành stream dùng sorted(phương thức sắp xếp)
        // true là tăng dần và ngược lại
        //reversed dùng để đảo ngược

    }



    public void toggleProductStatus(Integer id) {
        sanPhamRepository.findById(id).ifPresentOrElse(product -> {
            // Chỉ cho phép chuyển đổi giữa trạng thái 0 (Còn bán) và 2 (Ngừng bán)
            int newStatus = (product.getStatus() == 0) ? 2 : 0;
            product.setStatus(newStatus);
            sanPhamRepository.save(product);
        }, () -> {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        });
    }



    public void updateProductQuantity(Product product) {
        sanPhamRepository.save(product); // Lưu sản phẩm với số lượng mới
    }

    public Product laySanphamTheoId(Long sanphamId) {
        return sanPhamRepository.findById(sanphamId).orElse(null);
    }



    public Page<Product> searchProductsPaged(String keyword, Integer category, Pageable pageable) {
        return sanPhamRepository.findByFilters(keyword, category, pageable);
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTen(product.getTen());
        dto.setHinh(product.getHinh());
        dto.setGia(product.getGia());
        dto.setKichthuoc(product.getKichthuoc());
        dto.setConhang(product.isConhang());
        dto.setSoluong(product.getSoluong());
        dto.setMota(product.getMota());
        dto.setNgayTao(product.getNgayTao());
        if (product.getDanhMuc() != null) {
            dto.setCategoryId(product.getDanhMuc().getId());
            dto.setCategoryName(product.getDanhMuc().getTen());
        }
        return dto;
    }


}