package org.example.java5.dto;

import lombok.Data;
import org.example.java5.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Integer id;
    private String ten;
    private String hinh;
    private BigDecimal gia;
    private String kichthuoc;
    private boolean conhang;
    private Integer soluong;
    private String mota;
    private String danhMuc;
    private LocalDateTime ngayTao;
    private Integer status;

    public ProductResponse(Integer id, String ten, String hinh, BigDecimal gia, String kichthuoc, boolean conhang,
                           Integer soluong, String mota, String danhMuc, LocalDateTime ngayTao, Integer status) {
        this.id = id;
        this.ten = ten;
        this.hinh = hinh;
        this.gia = gia;
        this.kichthuoc = kichthuoc;
        this.conhang = conhang;
        this.soluong = soluong;
        this.mota = mota;
        this.danhMuc = danhMuc;
        this.ngayTao = ngayTao;
        this.status = status;
    }
    private ProductResponse convertToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTen(),
                product.getHinh(),
                product.getGia(),
                product.getKichthuoc(),
                product.isConhang(),
                product.getSoluong(),
                product.getMota(),
                product.getDanhMuc().getTen(),
                product.getNgayTao(),
                product.getStatus()
        );
    }

}
