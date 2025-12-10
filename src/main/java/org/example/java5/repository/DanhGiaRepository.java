package org.example.java5.repository;

import org.example.java5.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    boolean existsByDonHangId(int donHangId); // kiểm tra đã đánh giá chưa
    Optional<DanhGia> findByDonHangIdAndTaiKhoanId(Integer donHangId, Integer taiKhoanId);
    boolean existsByDonHangIdAndTaiKhoanId(Integer donHangId, Integer taiKhoanId);

}

