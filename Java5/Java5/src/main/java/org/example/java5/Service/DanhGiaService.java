package org.example.java5.Service;

import org.example.java5.dto.DanhGiaDTO;
import org.example.java5.entity.DanhGia;
import org.example.java5.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    public void submitDanhGia(DanhGiaDTO dto) {
        if (danhGiaRepo.findByDonHang_IdAndSanPham_IdAndTaiKhoan_Id(dto.getDonHangId(), dto.getSanPhamId(), dto.getTaiKhoanId()).isPresent()) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi!");
        }

        DanhGia dg = new DanhGia();
        dg.setDonHang(orderRepo.findById(dto.getDonHangId()).orElseThrow());
        dg.setSanPham(productRepo.findById(dto.getSanPhamId()).orElseThrow());
        dg.setTaiKhoan(userRepo.findById(dto.getTaiKhoanId()).orElseThrow());
        dg.setDanhgia(dto.getDanhgia());
        dg.setSao(dto.getSao());
        dg.setNgayDanhgia(LocalDate.now());

        danhGiaRepo.save(dg);
    }
    @GetMapping("/sanpham/{productId}")

//    public List<DanhGia> getReviewsByProductId(Long productId) {
//        return danhGiaRepo.findByProductId(productId);
//    }

    public List<DanhGiaDTO> getAllDanhGia() {
        List<DanhGia> danhGias = danhGiaRepo.findAll(); // Lấy tất cả các đánh giá từ repository
        return danhGias.stream()  // Chuyển đổi sang danh sách DTO
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DanhGiaDTO convertToDTO(DanhGia danhGia) {
        DanhGiaDTO dto = new DanhGiaDTO();
        // Lấy thông tin từ entity DanhGia và gán vào DTO

        dto.setDonHangId(danhGia.getDonHang().getId()); // Lấy id đơn hàng
        dto.setSanPhamId(danhGia.getSanPham().getId()); // Lấy id sản phẩm
        dto.setTaiKhoanId(danhGia.getTaiKhoan().getId()); // Lấy id người dùng
        dto.setDanhgia(danhGia.getDanhgia()); // Nội dung đánh giá
        dto.setSao(danhGia.getSao()); // Số sao
        dto.setNgayDanhgia(LocalDate.parse(danhGia.getNgayDanhgia().toString())); // Ngày đánh giá


        return dto;
    }
    public List<DanhGia> getReviewsByProductId(Long productId) {
        return danhGiaRepo.findBySanPhamId(productId);
    }
}
