package org.example.java5.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.java5.Service.GiohangService;
import org.example.java5.Service.SanPhamService;
import org.example.java5.dto.GiohangReponse;
import org.example.java5.entity.Giohang;
import org.example.java5.entity.Product;
import org.example.java5.entity.User;
import org.example.java5.jwt.JwtUtil;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user/giohang")
public class GiohangApiController {

    @Autowired
    private GiohangService giohangService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Lấy user từ token
    private Optional<User> getUserFromToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                return Optional.empty();
            }
            token = jwtUtil.cleanToken(token); // bỏ Bearer nếu có
            String email = jwtUtil.extractEmail(token);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // ✅ Lấy giỏ hàng theo token
    @GetMapping
    public ResponseEntity<?> hienThiGiohang(HttpServletRequest request) {
        Optional<User> userOptional = getUserFromToken(request);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        Integer taikhoanId = userOptional.get().getId();
        List<Giohang> gioHang = giohangService.getAllByTaikhoanId(taikhoanId);
        List<GiohangReponse> responseList = gioHang.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    // ✅ Thêm sản phẩm vào giỏ hàng
    @PostMapping("/them")
    public ResponseEntity<?> themVaoGiohang(@RequestParam Long sanphamId,
                                            @RequestParam int soLuong,
                                            HttpServletRequest request) {
        Optional<User> userOptional = getUserFromToken(request);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        Integer taikhoanId = userOptional.get().getId();
        Product sanpham = sanPhamService.laySanphamTheoId(sanphamId);
        if (sanpham == null) {
            return ResponseEntity.status(404).body("Sản phẩm không tồn tại");
        }

        if (sanpham.getStatus() == 2) {
            return ResponseEntity.status(400).body("Sản phẩm này hiện không thể mua");
        }

        int soLuongTonKho = sanpham.getSoluong();
        int soLuongHienTaiTrongGio = giohangService.laySoLuongSanphamTrongGio(taikhoanId, sanphamId);
        int tongSoLuongMuonThem = soLuongHienTaiTrongGio + soLuong;

        if (tongSoLuongMuonThem > soLuongTonKho) {
            return ResponseEntity.status(400).body("Số lượng sản phẩm trong kho không đủ");
        }

        giohangService.themVaoGiohang(taikhoanId, sanphamId, soLuong);
        return ResponseEntity.ok("Thêm sản phẩm vào giỏ hàng thành công");
    }

    // ✅ Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/capnhat")
    public ResponseEntity<?> capNhatSoLuong(@RequestParam Integer giohangId,
                                            @RequestParam int soLuong,
                                            HttpServletRequest request) {
        Optional<User> userOptional = getUserFromToken(request);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        giohangService.capNhatSoLuong(giohangId, soLuong);
        return ResponseEntity.ok("Cập nhật số lượng thành công");
    }

    // ✅ Xóa sản phẩm khỏi giỏ hàng (chỉ người đó được xóa)
    @DeleteMapping("/xoa/{sanphamId}")
    public ResponseEntity<?> xoaSanPhamTrongGiohang(HttpServletRequest request,
                                                    @PathVariable Integer sanphamId) {
        Optional<User> userOptional = getUserFromToken(request);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        Integer taikhoanId = userOptional.get().getId();
        giohangService.xoaGiohangTheoTaikhoan(taikhoanId, sanphamId);
        return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công");
    }

    // ✅ Trả về toàn bộ giỏ hàng của người dùng
    @GetMapping("/all")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        Optional<User> userOptional = getUserFromToken(request);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        Integer taikhoanId = userOptional.get().getId();
        List<Giohang> cartItems = giohangService.getAllByTaikhoanId(taikhoanId);
        List<GiohangReponse> responseList = cartItems.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/user/info")
    public ResponseEntity<?> getCurrentUserInfo(HttpServletRequest request) {
        Optional<User> userOpt = getUserFromToken(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Chưa đăng nhập");
        }

        User user = userOpt.get();
        Map<String, Object> info = new HashMap<>();
        info.put("name", user.getFullname() != null ? user.getFullname() : "");
        info.put("email", user.getEmail());
        info.put("sdt", user.getPhoneNumber() != null ? user.getPhoneNumber() : ""); // Quan trọng: trả về SĐT

        return ResponseEntity.ok(info);
    }

    // ✅ Convert sang Response DTO
    private GiohangReponse convertToResponse(Giohang giohang) {
        // Tính lại totalPrice để đảm bảo không bao giờ null
        BigDecimal totalPrice = giohang.getSanpham().getGia()
                .multiply(BigDecimal.valueOf(giohang.getSoLuong()));

        return new GiohangReponse(
                giohang.getId(),
                giohang.getTaikhoan().getId(),
                giohang.getSanpham().getId(),
                giohang.getSanpham().getTen(),
                giohang.getSoLuong(),
                totalPrice  // ← luôn có giá trị, không null
        );
    }
}
