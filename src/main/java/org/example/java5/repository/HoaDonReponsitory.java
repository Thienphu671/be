package org.example.java5.repository;

import org.example.java5.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonReponsitory extends JpaRepository<HoaDon, Long> {

    // Lấy thông tin khách hàng theo ID hóa đơn
    @Query(value = """
        SELECT 
            u.hoten AS tenKhachHang,
            u.email AS email,
            u.sdt AS soDienThoai,
            dh.diachi AS diaChi
        FROM hoadon hd
        JOIN taikhoan u ON hd.taikhoan_id = u.id
        JOIN donhang dh ON hd.donhang_id = dh.id
        WHERE hd.id = :id
    """, nativeQuery = true)
    Object[] getHoaDonThongTinKhachHang(@Param("id") int id);

    // Lấy chi tiết đơn hàng theo ID hóa đơn
    @Query(value = """
        SELECT 
            sp.hinhanh AS hinhSanPham,
            sp.tensanpham AS tenSanPham,
            dhct.soluong AS soLuong,
            sp.kichthuoc AS kichThuoc,
            sp.gia AS giaSanPham,
            (dhct.soluong * sp.gia) AS tongTien
        FROM chitietdonhang dhct
        JOIN sanpham sp ON dhct.sanpham_id = sp.id
        WHERE dhct.donhang_id = (SELECT donhang_id FROM hoadon WHERE id = :id)
    """, nativeQuery = true)
    List<Object[]> getHoaDonChiTiet(@Param("id") int id);

    // Lấy danh sách tất cả hóa đơn để hiển thị danh sách
    @Query(value = """
        SELECT hd.id, 
               RIGHT('000' + CAST(hd.id AS VARCHAR), 3) + CONVERT(VARCHAR, hd.ngaytao, 12) AS maHoaDon
        FROM hoadon hd
    """, nativeQuery = true)
    List<Object[]> getAllHoaDonIds();
}
