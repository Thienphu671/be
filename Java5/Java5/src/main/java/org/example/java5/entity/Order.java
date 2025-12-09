package org.example.java5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "donhang")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "taikhoan_id", nullable = false) // Liên kết với bảng user
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @Column(name = "ngaytao")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(name = "diachi",  nullable = false, columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "trangthai")
    private int status =0;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)

    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

}
