package org.example.java5.repository;

import org.example.java5.entity.Giohang;
import org.example.java5.entity.User;
import org.example.java5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GiohangRepository extends JpaRepository<Giohang, Integer> {

    // Kiểm tra sản phẩm đã tồn tại trong giỏ hàng chưa
    Optional<Giohang> findByTaikhoanAndSanpham(User taikhoan, Product sanpham);

    @Modifying
    @Query(value = "UPDATE giohang SET tongtien = ?2 WHERE id = ?1", nativeQuery = true)
    void capNhatTongTien(int id, BigDecimal tongTien);


    // ✅ Cập nhật số lượng sản phẩm trong giỏ hàng
    @Modifying
    @Query(value = "UPDATE giohang SET soluong = ?2 WHERE id = ?1", nativeQuery = true)
    void capNhatSoLuong(int id, int soLuong);

    // ✅ Xóa sản phẩm khỏi giỏ hàng theo tài khoản và sản phẩm
    @Modifying
    @Query(value = "DELETE FROM giohang WHERE taikhoan_id = ?1 AND sanpham_id = ?2", nativeQuery = true)
    void xoaSanPhamTrongGiohang(Integer taikhoanId, Integer sanphamId);

    // ✅ Lấy tất cả sản phẩm trong giỏ hàng của một tài khoản
    @Query(value = "SELECT * FROM giohang WHERE taikhoan_id = ?1", nativeQuery = true)
    List<Giohang> findAllByTaikhoanId(Integer taikhoanId);

    Optional<Giohang> findByTaikhoanIdAndSanphamId(int taikhoanId, long sanphamId);
}
