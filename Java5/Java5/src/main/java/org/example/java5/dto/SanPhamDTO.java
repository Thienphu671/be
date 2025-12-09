package org.example.java5.dto;

import lombok.Data;

@Data
public class SanPhamDTO {
    private Integer id;
    private String ten;
    private String hinhAnh;

    public SanPhamDTO() {
    }

    public SanPhamDTO(Integer id, String ten, String hinhAnh) {
        this.id = id;
        this.ten = ten;
        this.hinhAnh = hinhAnh;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
