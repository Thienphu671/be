//package org.example.java5.api;
//
//import org.example.java5.Service.SanPhamService;
//import org.example.java5.dto.ProductResponse;
//import org.example.java5.entity.Product;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//
//import java.io.File;
//
//@RestController
//@RequestMapping("/api/sanPhamND")
//public class SanPhamApiController {
//
//    @Autowired
//    private SanPhamService sanPhamService;
//
//    // Endpoint để trả ảnh từ thư mục D:/Java5/uploads/
//    @GetMapping("/images/{filename}")
//    public ResponseEntity<FileSystemResource> getImage(@PathVariable String filename) {
//        try {
//            File file = new File("D:/Java5/uploads/" + filename);
//            if (file.exists()) {
//                FileSystemResource resource = new FileSystemResource(file);
//                HttpHeaders headers = new HttpHeaders();
//                headers.add("Content-Type", "image/jpeg");  // Đảm bảo loại MIME của ảnh là đúng, ví dụ image/jpeg cho ảnh .jpg
//                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getSanPham(
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "category", required = false) Integer category,
//            @RequestParam(value = "sort", required = false) String sort,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "8") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Product> productPage = sanPhamService.searchProductsPaged(keyword, category, pageable);
//
//        // Lọc sản phẩm có status = 0
//        List<Product> filteredProducts = productPage.getContent().stream()
//                .filter(product -> product.getStatus() == 0)
//                .collect(Collectors.toList());
//
//        // Sắp xếp theo giá nếu có
//        if (sort != null && (sort.equalsIgnoreCase("asc") || sort.equalsIgnoreCase("desc"))) {
//            boolean ascending = "asc".equalsIgnoreCase(sort);
//            filteredProducts = sanPhamService.sortByPrice(filteredProducts, ascending);
//        }
//
//        // Chuyển sang DTO và sửa hinh để trả về URL
//        List<ProductResponse> productResponses = filteredProducts.stream()
//                .map(product -> new ProductResponse(
//                        product.getId(),
//                        product.getTen(),
//                        "/api/sanPhamND/images/" + product.getHinh(),  // Đổi tên file thành URL ảnh
//                        product.getGia(),
//                        product.getKichthuoc(),
//                        product.isConhang(),
//                        product.getSoluong(),
//                        product.getMota(),
//                        product.getDanhMuc().getTen(),
//                        product.getNgayTao(),
//                        product.getStatus()
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(Map.of(
//                "products", productResponses,
//                "currentPage", page,
//                "totalPages", productPage.getTotalPages(),
//                "pageSize", size
//        ));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponse> getSanPhamById(@PathVariable Long id) {
//        try {
//            Product product = sanPhamService.laySanphamTheoId(id);
//
//            // Kiểm tra nếu không tìm thấy sản phẩm
//            if (product == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            // Chuyển sang DTO và sửa hinh để trả về URL
//            ProductResponse productResponse = new ProductResponse(
//                    product.getId(),
//                    product.getTen(),
//                    "/api/sanPhamND/images/" + product.getHinh(),  // Đổi tên file thành URL ảnh
//                    product.getGia(),
//                    product.getKichthuoc(),
//                    product.isConhang(),
//                    product.getSoluong(),
//                    product.getMota(),
//                    product.getDanhMuc().getTen(),
//                    product.getNgayTao(),
//                    product.getStatus()
//            );
//
//            return ResponseEntity.ok(productResponse);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//
//}

package org.example.java5.api;

import org.example.java5.Service.SanPhamService;
import org.example.java5.dto.ProductResponse;
import org.example.java5.entity.Product;
import org.example.java5.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sanPhamND")
public class SanPhamApiController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    // Trả ảnh từ thư mục
    @GetMapping("/images/{filename}")
    public ResponseEntity<FileSystemResource> getImage(@PathVariable String filename) {
        try {
            File file = new File("D:/Java5/uploads/" + filename);
            if (file.exists()) {
                FileSystemResource resource = new FileSystemResource(file);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "image/jpeg");
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Lấy danh sách sản phẩm có phân trang, tìm kiếm, lọc theo danh mục, sắp xếp
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSanPham(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) Integer category,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = sanPhamService.searchProductsPaged(keyword, category, pageable);

        // Lọc và sắp xếp
        List<Product> filteredProducts = productPage.getContent().stream()
                .filter(p -> p.getStatus() == 0)
                .collect(Collectors.toList());

        if ("asc".equalsIgnoreCase(sort) || "desc".equalsIgnoreCase(sort)) {
            filteredProducts = sanPhamService.sortByPrice(filteredProducts, "asc".equalsIgnoreCase(sort));
        }

        // Chuyển sang ProductResponse
        List<ProductResponse> productResponses = filteredProducts.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getTen(),
                        "/api/sanPhamND/images/" + product.getHinh(),
                        product.getGia(),
                        product.getKichthuoc(),
                        product.isConhang(),
                        product.getSoluong(),
                        product.getMota(),
                        product.getDanhMuc() != null ? product.getDanhMuc().getTen() : null,
                        product.getNgayTao(),
                        product.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "products", productResponses,
                "currentPage", page,
                "totalPages", productPage.getTotalPages(),
                "pageSize", size
        ));
    }




    // Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getSanPhamById(@PathVariable Long id) {
        Product product = sanPhamService.laySanphamTheoId(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getTen(),
                "/api/sanPhamND/images/" + product.getHinh(),
                product.getGia(),
                product.getKichthuoc(),
                product.isConhang(),
                product.getSoluong(),
                product.getMota(),
                product.getDanhMuc() != null ? product.getDanhMuc().getTen() : null,
                product.getNgayTao(),
                product.getStatus()
        );

        return ResponseEntity.ok(response);
    }



    // Endpoint lấy top 8 sản phẩm mới
    @GetMapping("/top8")
    public ResponseEntity<List<ProductResponse>> getTop8SanPhamMoi() {
        try {
            List<Product> topProducts = sanPhamRepository.findTop8ByStatusOrderByIdDesc(0);

            List<ProductResponse> productResponses = topProducts.stream()
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getTen(),
                            "/api/sanPhamND/images/" + product.getHinh(),
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

            return ResponseEntity.ok(productResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Lấy sản phẩm mới nhất theo ID lớn nhất và status = 0
    @GetMapping("/sapXepMoiNhat")
    public ResponseEntity<List<ProductResponse>> getSanPhamSapXepMoiNhat() {
        try {
            // Lấy sản phẩm có status = 0, sắp xếp theo id giảm dần và ngày tạo giảm dần
            List<Product> products = sanPhamRepository.findAllByStatusOrderByIdDesc(0);

            List<ProductResponse> productResponses = products.stream()
                    .map(product -> new ProductResponse(
                            product.getId(),
                            product.getTen(),
                            "/api/sanPhamND/images/" + product.getHinh(),
                            product.getGia(),
                            product.getKichthuoc(),
                            product.isConhang(),
                            product.getSoluong(),
                            product.getMota(),
                            product.getDanhMuc() != null ? product.getDanhMuc().getTen() : null,
                            product.getNgayTao(),
                            product.getStatus()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(productResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}

