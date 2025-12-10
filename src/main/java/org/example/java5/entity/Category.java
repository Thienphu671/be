package org.example.java5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "danhmuc")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten",columnDefinition = "NVARCHAR(255)")
    private String ten;

    @Column(name = "trangthai")
    private int status = 0;

    @OneToMany(mappedBy = "danhMuc")
    @JsonIgnore

    private List<Product> sanPham;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Product> getSanPham() {
        return sanPham;
    }

    public void setSanPham(List<Product> sanPham) {
        this.sanPham = sanPham;
    }
}
