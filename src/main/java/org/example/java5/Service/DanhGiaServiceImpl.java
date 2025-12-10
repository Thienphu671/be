package org.example.java5.Service;


import org.example.java5.entity.DanhGia;
import org.example.java5.repository.DanhGiaRepository;
import org.example.java5.Service.DanhGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanhGiaServiceImpl implements DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    @Override
    public boolean hasReviewed(Integer donHangId, Integer taiKhoanId) {
        return danhGiaRepository.existsByDonHangIdAndTaiKhoanId(donHangId, taiKhoanId);
    }

    @Override
    public DanhGia save(DanhGia danhGia) {
        return danhGiaRepository.save(danhGia);
    }
}
