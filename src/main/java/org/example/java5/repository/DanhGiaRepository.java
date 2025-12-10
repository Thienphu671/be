package org.example.java5.repository;

import org.example.java5.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    boolean existsByDonHangId(int donHangId); // kiểm tra đã đánh giá chưa
    Optional<DanhGia> findByDonHangIdAndTaiKhoanId(Integer donHangId, Integer taiKhoanId);
    boolean existsByDonHangIdAndTaiKhoanId(Integer donHangId, Integer taiKhoanId);
        List<DanhGia> findBySanPham_Id(Integer sanPhamId); // để hiển thị ở trang chi tiết sản phẩm
    Optional<DanhGia> findByDonHang_IdAndSanPham_IdAndTaiKhoan_Id(Integer donHangId, Integer sanPhamId, Integer taiKhoanId);
//    List<DanhGia> findByProductId(Long productId);
    List<DanhGia> findBySanPhamId(Long productId);


}

