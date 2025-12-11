package org.example.java5.controllers.admin;

import org.example.java5.Service.SanPhamService;
import org.example.java5.Service.CategoryService;
import org.example.java5.entity.Category;
import org.example.java5.entity.Favorite;
import org.example.java5.entity.Product;
import org.example.java5.repository.FavoriteRepository;
import org.example.java5.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping("/admin/sanpham")
public class SanPhamAdminController {

    private static final String UPLOAD_DIR = "C:/Users/PC/Documents/GitHub/be/uploads";

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping
    public String index(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Product> products = (keyword != null && !keyword.trim().isEmpty())
                ? sanPhamService.searchByName(keyword.trim())
                : sanPhamService.getAll();

        // Cập nhật tất cả sản phẩm trước khi hiển thị
        for (Product product : products) {
            sanPhamService.update(product);
        }

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "admin/adminSanPham";
    }


    @GetMapping("/form")
    public String showForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Product product = (id != null) ? sanPhamService.getById(id).orElse(new Product()) : new Product();

        // Nếu sản phẩm có ảnh cũ, thêm vào model để hiển thị trong form
        if (product.getHinh() != null && !product.getHinh().isEmpty()) {
            model.addAttribute("oldImage", product.getHinh());
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());

        return "admin/sanPhamForm";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute Product product,
                       @RequestParam("file") MultipartFile file,
                       @RequestParam("categoryId") Integer categoryId,

                       @RequestParam(value = "kichthuoc", required = false) String kichthuoc,
                       RedirectAttributes redirectAttributes) {
        Map<String, String> errors = new HashMap<>();
        System.out.println("Giá trị kichThuoc từ request: " + kichthuoc);


        // Kiểm tra dữ liệu đầu vào
        if (product.getTen() == null || product.getTen().trim().isEmpty()) {
            errors.put("ten", "Tên sản phẩm không được để trống");
        } else {
            // Kiểm tra trùng tên sản phẩm
            Optional<Product> existingProduct = sanPhamRepository.findByTen(product.getTen().trim());
            if (existingProduct.isPresent() && (product.getId() == null || !existingProduct.get().getId().equals(product.getId()))) {
                errors.put("ten", "Tên sản phẩm đã tồn tại.");
            }
        }
        if (product.getGia() == null || product.getGia().compareTo(new BigDecimal(10000)) < 0) {
            errors.put("gia", "Giá tiền không được dưới 10.000 VND");
        }
        if (product.getSoluong() == null || product.getSoluong() <= 0) {
            errors.put("soluong", "Số lượng phải lớn hơn 0");
        }
        if (product.getMota() == null || product.getMota().length() < 10 || product.getMota().length() > 200) {
            errors.put("mota", "Mô tả phải từ 10 đến 200 ký tự");
        }


        product.setKichthuoc(kichthuoc);

        // Kiểm tra các trường khác như category, loai...
        Optional<Category> categoryOpt = categoryService.getById(categoryId);
        if (categoryOpt.isEmpty()) {
            errors.put("category", "Danh mục không hợp lệ");
        }


        // Kiểm tra hình ảnh
        if (!file.isEmpty()) {
            // Nếu có ảnh mới, xử lý ảnh
            try {
                String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setHinh(fileName);  // Cập nhật ảnh mới
            } catch (IOException e) {
                errors.put("hinh", "Lỗi khi lưu file: " + e.getMessage());
            }
        } else {
            // Nếu không có file mới, kiểm tra xem đã có ảnh cũ (được gửi từ form thông qua field ẩn) hay chưa
            if (product.getHinh() == null || product.getHinh().isEmpty()) {
                errors.put("hinh", "Hình ảnh không được để trống");
            }
        }

        // Nếu có lỗi, trả lại form và hiển thị lỗi
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/admin/sanpham/form";  // Đảm bảo quay lại form
        }

        // Gán danh mục và loại sản phẩm
        product.setDanhMuc(categoryOpt.get());


        // Phân biệt giữa thêm mới và cập nhật
        if (product.getId() != null) {
            // Gọi phương thức update() để xử lý logic giữ ảnh cũ nếu không có ảnh mới
            sanPhamService.update(product);
        } else {
            sanPhamService.save(product);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được lưu thành công");
        return "redirect:/admin/sanpham";  // Quay lại danh sách sản phẩm
    }



    @DeleteMapping("/admin/yeuthich/{id}")
    public ResponseEntity<String> deleteYeuThich(@PathVariable Integer id) {
        Optional<Favorite> favorite = favoriteRepository.findById(id);

        if (favorite.isPresent()) {
            int sanPhamId = favorite.get().getProduct().getId();

            // Xóa yêu thích
            favoriteRepository.delete(favorite.get());

            // Sau đó xóa luôn sản phẩm
            sanPhamRepository.deleteById(sanPhamId);

            return ResponseEntity.ok("Đã xóa yêu thích và sản phẩm liên quan!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy mục yêu thích!");
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Integer id) {
        sanPhamService.toggleProductStatus(id);
        return "redirect:/admin/sanpham";
    }



}
