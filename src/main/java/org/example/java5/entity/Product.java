package org.example.java5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "sanpham")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Column(name = "ten", nullable = false, length = 255)
    private String ten;

    @Column(name = "hinh", length = 250) // Lưu tên file ảnh
    private String hinh;

    @NotNull(message = "Giá tiền không được để trống")
    @DecimalMin(value = "10000.0", message = "Giá tiền không được dưới 10.000 VND")
    @Column(name = "gia", nullable = false)
    private BigDecimal gia;

    @NotNull(message = "Phải chọn kích thước")
    @Column(name = "kichthuoc", nullable = false, length = 50)
    private String kichthuoc;

    @Column(name = "conhang", nullable = false)
    private boolean conhang;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải ít nhất là 0")
    @Column(name = "soluong", nullable = false)
    private Integer soluong;

    @NotNull(message = "Mô tả không được để trống")
    @Size(min = 10, max = 200, message = "Mô tả phải từ 10 đến 200 ký tự")

    @Column(name = "mota", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String mota;

    //nho hoc
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Favorite> danhSachYeuThich;

    @OneToMany(mappedBy = "sanpham", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Giohang> gioHangs;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "danhmuc_id", nullable = false)
    @NotNull(message = "Danh mục không được để trống")
    private Category danhMuc;

    @Column(name = "ngaytao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "trangthai",nullable = false)

    private Integer status = 0;

    // Hàm tự động gán ngày tạo khi lưu vào database
    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
    }

//    public enum ProductType {
//        NAM, NU, UNISEX;
//    }


    public Product() {
    }

    public Product(Integer id, String ten, String hinh, BigDecimal gia, String kichthuoc, boolean conhang, Integer soluong, String mota, List<Favorite> danhSachYeuThich, List<Giohang> gioHangs, Category danhMuc, LocalDateTime ngayTao, Integer status) {
        this.id = id;
        this.ten = ten;
        this.hinh = hinh;
        this.gia = gia;
        this.kichthuoc = kichthuoc;
        this.conhang = conhang;
        this.soluong = soluong;
        this.mota = mota;
        this.danhSachYeuThich = danhSachYeuThich;
        this.gioHangs = gioHangs;
        this.danhMuc = danhMuc;
        this.ngayTao = ngayTao;
        this.status = status;
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "Tên sản phẩm không được để trống") String getTen() {
        return ten;
    }
    public void setTen(@NotBlank(message = "Tên sản phẩm không được để trống") String ten) {
        this.ten = ten;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public @NotNull(message = "Giá tiền không được để trống") @DecimalMin(value = "10000.0", message = "Giá tiền không được dưới 10.000 VND") BigDecimal getGia() {
        return gia;
    }

    public void setGia(@NotNull(message = "Giá tiền không được để trống") @DecimalMin(value = "10000.0", message = "Giá tiền không được dưới 10.000 VND") BigDecimal gia) {
        this.gia = gia;
    }

    public @NotNull(message = "Phải chọn kích thước") String getKichthuoc() {
        return kichthuoc;
    }

    public void setKichthuoc(@NotNull(message = "Phải chọn kích thước") String kichthuoc) {
        this.kichthuoc = kichthuoc;
    }

    public boolean isConhang() {
        return conhang;
    }

    public void setConhang(boolean conhang) {
        this.conhang = conhang;
    }

    public @NotNull(message = "Số lượng không được để trống") @Min(value = 0, message = "Số lượng phải ít nhất là 0") Integer getSoluong() {
        return soluong;
    }

    public void setSoluong(Integer soluong) {
        this.soluong = soluong;

        if (this.soluong > 0) {
            // Nếu trạng thái đang là 1, đổi về 0 mặc định
            if (this.status == 1) {
                this.status = 0;
            }
            // Nếu trạng thái không phải 2, đặt về 0
            else if (this.status != 2) {
                this.status = 0;
            }
        } else {
            // Nếu số lượng = 0, trạng thái = 1 (hết hàng)
            this.status = 1;
        }
    }

    public @NotNull(message = "Mô tả không được để trống") @Size(min =10 , max = 200, message = "Mô tả phải từ 10 đến 200 ký tự") String getMota() {
        return mota;
    }

    public void setMota(@NotNull(message = "Mô tả không được để trống") @Size(min = 10, max = 200, message = "Mô tả phải từ 10 đến 200 ký tự") String mota) {
        this.mota = mota;
    }



    public @NotNull(message = "Danh mục không được để trống") Category getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(@NotNull(message = "Danh mục không được để trống") Category danhMuc) {
        this.danhMuc = danhMuc;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}