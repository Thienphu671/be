package org.example.java5.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sanpham_id")
    private Product sanPham;

    @ManyToOne
    @JoinColumn(name = "donhang_id")
    private Order donHang;

    @ManyToOne
    @JoinColumn(name = "taikhoan_id")
    private User taiKhoan;

    @Column(name = "danhgia", columnDefinition = "NVARCHAR(MAX)")
    private String danhgia;    private int sao;
    private LocalDate ngayDanhgia;

    public DanhGia() {
    }

    public DanhGia(Integer id, Product sanPham, Order donHang, User taiKhoan, String danhgia, int sao, LocalDate ngayDanhgia) {
        this.id = id;
        this.sanPham = sanPham;
        this.donHang = donHang;
        this.taiKhoan = taiKhoan;
        this.danhgia = danhgia;
        this.sao = sao;
        this.ngayDanhgia = ngayDanhgia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getSanPham() {
        return sanPham;
    }

    public void setSanPham(Product sanPham) {
        this.sanPham = sanPham;
    }

    public Order getDonHang() {
        return donHang;
    }

    public void setDonHang(Order donHang) {
        this.donHang = donHang;
    }

    public User getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(User taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }

    public int getSao() {
        return sao;
    }

    public void setSao(int sao) {
        this.sao = sao;
    }

    public LocalDate getNgayDanhgia() {
        return ngayDanhgia;
    }

    public void setNgayDanhgia(LocalDate ngayDanhgia) {
        this.ngayDanhgia = ngayDanhgia;
    }
// Constructor, Getter, Setter...
}
