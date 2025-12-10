package org.example.java5.dto;

import lombok.Data;

@Data
public class DanhGiaDTO {
    private int donhangId;
    private int taikhoanId;
    private String danhgia;
    private int sao;

    public DanhGiaDTO() {
    }

    public DanhGiaDTO(int donhangId, int taikhoanId, String danhgia, int sao) {
        this.donhangId = donhangId;
        this.taikhoanId = taikhoanId;
        this.danhgia = danhgia;
        this.sao = sao;
    }

    public int getDonhangId() {
        return donhangId;
    }

    public void setDonhangId(int donhangId) {
        this.donhangId = donhangId;
    }

    public int getTaikhoanId() {
        return taikhoanId;
    }

    public void setTaikhoanId(int taikhoanId) {
        this.taikhoanId = taikhoanId;
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
}
