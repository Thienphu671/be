package org.example.java5.controllers.nguoiDung;

import org.example.java5.dto.DanhGiaDTO;
import org.example.java5.dto.SanPhamDTO;
import org.example.java5.entity.DanhGia;
import org.example.java5.repository.DanhGiaRepository;
import org.example.java5.Service.DanhGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.java5.entity.Product;

import java.util.List;

@RestController
@RequestMapping("/api/danhgia")
public class DanhGiaController {

    @Autowired
    private DanhGiaService danhGiaService;

    @Autowired
    private DanhGiaRepository danhGiaRepo;

    @PostMapping("/submit")
    public String submit(@RequestBody DanhGiaDTO dto) {
        danhGiaService.submitDanhGia(dto);
        return "Đánh giá thành công!";
    }

    @GetMapping("/sanpham/{id}")
    public List<DanhGiaDTO> getDanhGiaBySanPham(@PathVariable("id") Integer sanPhamId) {
        return danhGiaRepo.findBySanPham_Id(sanPhamId).stream().map(dg -> {
            DanhGiaDTO dto = new DanhGiaDTO();


            dto.setDonHangId(dg.getDonHang().getId());
            dto.setSanPhamId(dg.getSanPham().getId());
            dto.setTaiKhoanId(dg.getTaiKhoan().getId());
            dto.setDanhgia(dg.getDanhgia());
            dto.setSao(dg.getSao());
            dto.setNgayDanhgia(dg.getNgayDanhgia());

            dto.setTenNguoiDung(dg.getTaiKhoan().getFullname()); // hoặc .getFullName(), tùy bạn

            return dto;
        }).toList();
    }

    @GetMapping("/all")
    public List<DanhGiaDTO> getAllDanhGia() {
        return danhGiaService.getAllDanhGia();
    }
//    @GetMapping("/sanpham/{productId}")
//    public List<DanhGia> getProductReviews(@PathVariable("productId") Long productId) {
//        return danhGiaService.getReviewsByProductId(productId);
//    }
}
