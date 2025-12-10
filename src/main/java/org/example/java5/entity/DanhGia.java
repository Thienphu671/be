package org.example.java5.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DanhGia {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "donhang_id")
    private Order donHang;

    @ManyToOne
    @JoinColumn(name = "taikhoan_id")
    private User taiKhoan;

    private String danhgia;
    private int sao;
    private LocalDate ngayDanhgia;

    public DanhGia() {
    }

    public DanhGia(Integer id, Order donHang, User taiKhoan, String danhgia, int sao, LocalDate ngayDanhgia) {
        this.id = id;
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
    // Getter, Setter
}
