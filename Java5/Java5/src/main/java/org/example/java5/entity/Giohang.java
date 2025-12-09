package org.example.java5.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "giohang")
public class Giohang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "taikhoan_id", nullable = false)
    private User taikhoan;

    @ManyToOne
    @JoinColumn(name = "sanpham_id", nullable = false)
    private Product sanpham;

    @Column(name = "tensanpham", nullable = false)
    private String tenSanpham;

    @Column(name = "soluong", nullable = false)
    private int soLuong;

    @Column(name = "tongtien", nullable = false)
    private BigDecimal tongTien;

    @Column(name = "ngaytao", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao = LocalDateTime.now();

    public Giohang() {
    }

    public Giohang(int id, User taikhoan, Product sanpham, String tenSanpham, int soLuong, BigDecimal tongTien, LocalDateTime ngayTao) {
        this.id = id;
        this.taikhoan = taikhoan;
        this.sanpham = sanpham;
        this.tenSanpham = tenSanpham;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(User taikhoan) {
        this.taikhoan = taikhoan;
    }

    public Product getSanpham() {
        return sanpham;
    }

    public void setSanpham(Product sanpham) {
        this.sanpham = sanpham;
    }

    public String getTenSanpham() {
        return tenSanpham;
    }

    public void setTenSanpham(String tenSanpham) {
        this.tenSanpham = tenSanpham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}
