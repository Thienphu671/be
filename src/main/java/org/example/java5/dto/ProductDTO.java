package org.example.java5.dto;



import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Integer id;
    private String ten;
    private String hinh;
    private BigDecimal gia;
    private String kichthuoc;
    private boolean conhang;
    private Integer soluong;
    private String mota;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime ngayTao;
}
