package org.example.java5.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DanhGiaDTO {

    private Integer donHangId;
    private Integer sanPhamId; // <== thêm trường này
    private Integer taiKhoanId;
    private String danhgia;
    private int sao;
    private LocalDate ngayDanhgia;
    private String tenNguoiDung; // thêm trường mới


    public DanhGiaDTO() {
    }

    public DanhGiaDTO( Integer donHangId, Integer sanPhamId, Integer taiKhoanId, String danhgia, int sao, LocalDate ngayDanhgia) {

        this.donHangId = donHangId;
        this.sanPhamId = sanPhamId;
        this.taiKhoanId = taiKhoanId;
        this.danhgia = danhgia;
        this.sao = sao;
        this.ngayDanhgia = ngayDanhgia;
    }




    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }


    public Integer getDonHangId() {
        return donHangId;
    }

    public void setDonHangId(Integer donHangId) {
        this.donHangId = donHangId;
    }

    public Integer getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(Integer sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public Integer getTaiKhoanId() {
        return taiKhoanId;
    }

    public void setTaiKhoanId(Integer taiKhoanId) {
        this.taiKhoanId = taiKhoanId;
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
}
