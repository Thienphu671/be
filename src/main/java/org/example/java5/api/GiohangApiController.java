package org.example.java5.api;
import jakarta.servlet.http.HttpSession;
import org.example.java5.Service.GiohangService;
import org.example.java5.Service.SanPhamService;
import org.example.java5.entity.Giohang;
import org.example.java5.entity.Product;
import org.example.java5.entity.User;
import org.example.java5.dto.GiohangReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.java5.repository.UserRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/giohang")
class GiohangApiController {
    private final GiohangService giohangService;
    private final UserRepository userRepository;
    private final SanPhamService sanPhamService;

    @Autowired
    public GiohangApiController(GiohangService giohangService, UserRepository userRepository, SanPhamService sanPhamService) {
        this.giohangService = giohangService;
        this.userRepository = userRepository;
        this.sanPhamService = sanPhamService;
    }

    @GetMapping
    public ResponseEntity<?> hienThiGiohang(HttpSession session) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanId == null) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        Optional<User> userOptional = userRepository.findById(taikhoanId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy người dùng");
        }

        List<Giohang> gioHang = giohangService.getAllByTaikhoanId(taikhoanId);
        List<GiohangReponse> responseList = gioHang.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/them")
    public ResponseEntity<?> themVaoGiohang(@RequestParam Long sanphamId,
                                            @RequestParam int soLuong,
                                            HttpSession session) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanId == null) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

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

    @PostMapping("/capnhat")
    public ResponseEntity<?> capNhatSoLuong(@RequestParam Integer giohangId,
                                            @RequestParam int soLuong,
                                            HttpSession session) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanId == null) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        giohangService.capNhatSoLuong(giohangId, soLuong);
        return ResponseEntity.ok("Cập nhật số lượng thành công");
    }

    @DeleteMapping("/xoa/{taikhoanId}/{sanphamId}")
    public ResponseEntity<?> xoaSanPhamTrongGiohang(@PathVariable Integer taikhoanId,
                                                    @PathVariable Integer sanphamId) {
        giohangService.xoaGiohangTheoTaikhoan(taikhoanId, sanphamId);
        return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(HttpSession session) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanId == null) {
            return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");
        }

        List<Giohang> cartItems = giohangService.getAllByTaikhoanId(taikhoanId);
        List<GiohangReponse> responseList = cartItems.stream().map(this::convertToResponse).toList();
        return ResponseEntity.ok(responseList);
    }

    private GiohangReponse convertToResponse(Giohang giohang) {
        BigDecimal tongTien = giohang.getSanpham().getGia().multiply(BigDecimal.valueOf(giohang.getSoLuong())); // ✅ Định nghĩa trước

        return new GiohangReponse(
                giohang.getId(),
                giohang.getTaikhoan().getId(),
                giohang.getSanpham().getId(),
                giohang.getSoLuong(),
                tongTien  // ✅ Truyền giá trị đã khai báo
        );
    }

}