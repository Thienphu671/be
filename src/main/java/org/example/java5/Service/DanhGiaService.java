package org.example.java5.Service;

import org.example.java5.entity.DanhGia;

public interface DanhGiaService {
    boolean hasReviewed(Integer donHangId, Integer taiKhoanId);
    DanhGia save(DanhGia danhGia);
}
