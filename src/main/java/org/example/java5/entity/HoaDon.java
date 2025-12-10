package org.example.java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "hoadon")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Ma_donhang", nullable = false)
    private int maDonhang;

    @CreationTimestamp
    @Column(name = "ngaytao", updatable = false)
    private LocalDate ngayLap;

    @Column(name = "tongtien")
    private Double tongTien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donhang_id", nullable = false)
    private Order donhang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taikhoan_id", nullable = false)
    private User user;


}
