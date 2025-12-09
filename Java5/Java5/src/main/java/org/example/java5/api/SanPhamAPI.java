package org.example.java5.api;

import org.example.java5.Service.SanPhamService;
import org.example.java5.Service.CategoryService;
import org.example.java5.dto.ProductResponse;
import org.example.java5.entity.Category;
import org.example.java5.entity.Product;
import org.example.java5.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/sanpham")
public class SanPhamAPI {

    private static final String UPLOAD_DIR = "D:/Java5/uploads/";

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    // 🟢 API: Lấy danh sách sản phẩm
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> products = sanPhamService.getAll().stream()
                .map(product -> {
                    // Kiểm tra số lượng sản phẩm, nếu bằng 0 thì thay đổi trạng thái
                    if (product.getSoluong() == 0) {
                        product.setStatus(1);  // Giả sử trạng thái 1 là "hết hàng"
                    }

                    return new ProductResponse(
                            product.getId(),
                            product.getTen(),
                            product.getHinh(),
                            product.getGia(),
                            product.getKichthuoc(),
                            product.isConhang(),
                            product.getSoluong(),
                            product.getMota(),
                            product.getDanhMuc().getTen(),
                            product.getNgayTao(),
                            product.getStatus()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "message", "Lấy danh sách sản phẩm thành công!",
                "products", products
        ));
    }


    // 🟢 API: Lấy chi tiết sản phẩm
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Optional<Product> productOpt = sanPhamService.getById(id);
        return productOpt.map(product -> ResponseEntity.ok(Map.of(
                "message", "Lấy thông tin sản phẩm thành công!",
                "product", new ProductResponse(
                        product.getId(),
                        product.getTen(),
                        product.getHinh(),
                        product.getGia(),
                        product.getKichthuoc(),
                        product.isConhang(),
                        product.getSoluong(),
                        product.getMota(),
                        product.getDanhMuc().getTen(),
                        product.getNgayTao(),
                        product.getStatus()
                )
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Sản phẩm không tồn tại!")));
    }

    // 🟢 API: Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestParam("ten") String ten,
            @RequestParam("gia") BigDecimal gia,
            @RequestParam("kichthuoc") String kichthuoc,
            @RequestParam("soluong") Integer soluong,
            @RequestParam("mota") String mota,
            @RequestParam("status")Integer status,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Kiểm tra tên sản phẩm đã tồn tại chưa
        if (sanPhamRepository.existsByTen(ten)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên sản phẩm đã tồn tại!"));
        }

        // Kiểm tra giá phải lớn hơn 10,000
        if (gia.compareTo(BigDecimal.valueOf(10000)) <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Giá sản phẩm phải lớn hơn 10,000!"));
        }

        // Kiểm tra số lượng phải lớn hơn 0
        if (soluong <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số lượng sản phẩm phải lớn hơn 0!"));
        }

        // Kiểm tra mô tả từ 10 đến 200 ký tự
        if (mota.length() < 10 || mota.length() > 200) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mô tả sản phẩm phải có ít nhất 10 ký tự và tối đa 200 ký tự!"));
        }

        // Tiến hành thêm sản phẩm vào cơ sở dữ liệu
        Optional<Category> categoryOpt = categoryService.getById(categoryId);
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Danh mục không hợp lệ!"));
        }

        // Xử lý hình ảnh
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            try {
                fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi lưu file: " + e.getMessage()));
            }
        }

        Product product = new Product();
        product.setTen(ten);
        product.setGia(gia);
        product.setKichthuoc(kichthuoc);
        product.setSoluong(soluong);
        product.setMota(mota);
        product.setStatus(status);
        product.setDanhMuc(categoryOpt.get());
        product.setHinh(fileName);

        sanPhamService.save(product);

        return ResponseEntity.ok(Map.of(
                "message", "Thêm sản phẩm thành công!",
                "product", new ProductResponse(
                        product.getId(),
                        product.getTen(),
                        product.getHinh(),
                        product.getGia(),
                        product.getKichthuoc(),
                        product.isConhang(),
                        product.getSoluong(),
                        product.getMota(),
                        product.getDanhMuc().getTen(),
                        product.getNgayTao(),
                        product.getStatus()
                )
        ));
    }


    // 🟢 API: Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Integer id,
            @RequestParam("ten") String ten,
            @RequestParam("gia") BigDecimal gia,
            @RequestParam("kichthuoc") String kichthuoc,
            @RequestParam("soluong") Integer soluong,
            @RequestParam("mota") String mota,
            @RequestParam("status") Integer status,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Kiểm tra tên sản phẩm đã tồn tại chưa
        if (sanPhamRepository.existsByTen(ten)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên sản phẩm đã tồn tại!"));
        }

        // Kiểm tra giá phải lớn hơn 10,000
        if (gia.compareTo(BigDecimal.valueOf(10000)) <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Giá sản phẩm phải lớn hơn 10,000!"));
        }

        // Kiểm tra số lượng phải lớn hơn 0
        if (soluong <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số lượng sản phẩm phải lớn hơn 0!"));
        }

        // Kiểm tra mô tả từ 10 đến 200 ký tự
        if (mota.length() < 10 || mota.length() > 200) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mô tả sản phẩm phải có ít nhất 10 ký tự và tối đa 200 ký tự!"));
        }

        Optional<Product> productOpt = sanPhamService.getById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Sản phẩm không tồn tại!"));
        }

        Optional<Category> categoryOpt = categoryService.getById(categoryId);
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Danh mục không hợp lệ!"));
        }

        Product product = productOpt.get();
        product.setTen(ten);
        product.setGia(gia);
        product.setKichthuoc(kichthuoc);
        product.setSoluong(soluong);
        product.setMota(mota);
        product.setStatus(status);
        product.setDanhMuc(categoryOpt.get());

        // Kiểm tra số lượng, nếu bằng 0 thì tự động chuyển trạng thái về 1 (hết hàng)
        if (soluong == 0) {
            product.setStatus(1); // Chuyển trạng thái sang "hết hàng"
        }

        // Xử lý hình ảnh
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setHinh(fileName);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(Map.of("message", "Lỗi khi lưu file: " + e.getMessage()));
            }
        }

        sanPhamService.update(product);
        return ResponseEntity.ok(Map.of(
                "message", "Cập nhật sản phẩm thành công!",
                "product", new ProductResponse(
                        product.getId(),
                        product.getTen(),
                        product.getHinh(),
                        product.getGia(),
                        product.getKichthuoc(),
                        product.isConhang(),
                        product.getSoluong(),
                        product.getMota(),
                        product.getDanhMuc().getTen(),
                        product.getNgayTao(),
                        product.getStatus()
                )
        ));
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam(value = "name", required = false) String name) {
        List<ProductResponse> products;

        if (name != null && !name.isEmpty()) {
            // Tìm kiếm sản phẩm theo tên
            products = sanPhamService.getAll().stream()
                    .filter(product -> product.getTen().toLowerCase().contains(name.toLowerCase()))
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getTen(),
                            product.getHinh(),
                            product.getGia(),
                            product.getKichthuoc(),
                            product.isConhang(),
                            product.getSoluong(),
                            product.getMota(),
                            product.getDanhMuc().getTen(),
                            product.getNgayTao(),
                            product.getStatus()
                    ))
                    .collect(Collectors.toList());
        } else {
            // Lấy tất cả sản phẩm nếu không có tên tìm kiếm
            products = sanPhamService.getAll().stream()
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getTen(),
                            product.getHinh(),
                            product.getGia(),
                            product.getKichthuoc(),
                            product.isConhang(),
                            product.getSoluong(),
                            product.getMota(),
                            product.getDanhMuc().getTen(),
                            product.getNgayTao(),
                            product.getStatus()
                    ))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(Map.of(
                "message", "Lấy danh sách sản phẩm thành công!",
                "products", products
        ));
    }


    // API chuyển đổi trạng thái giữa 0 <=> 2
    @PostMapping("/toggle-status/{id}")
    public ResponseEntity<String> toggleStatus(@PathVariable Integer id) {
        try {
            sanPhamService.toggleProductStatus(id);
            return ResponseEntity.ok("Đã chuyển đổi trạng thái thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
