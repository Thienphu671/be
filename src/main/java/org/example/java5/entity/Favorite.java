package org.example.java5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "yeuthich")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "taikhoan_id", nullable = false) // Liên kết với bảng taikhoan
    private User user;

    @ManyToOne
    @JoinColumn(name = "sanpham_id", nullable = false) // Liên kết với bảng sanpham
    private Product product;

    @Column(name = "ngaythich")
    @Temporal(TemporalType.DATE)
    private Date likedDate;
    @Column(name = "trangthai", nullable = false)
    private boolean status = false;


}
