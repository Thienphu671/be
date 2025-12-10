package org.example.java5.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "taikhoan")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "matkhau", length = 255, nullable = false)
    private String password;

    @Column(name = "hoten", length = 60, nullable = false)
    private String fullname;

    @Column(name = "sdt", length = 15, nullable = false, unique = true)

    private String phoneNumber; // Thêm số điện thoại

    @Column(name = "hinh", length = 255)
    private String photo;

    @Column(name = "kichhoat", nullable = false)
    private Boolean activated = true;

    @Column(name = "quantri", nullable = false)
    private Boolean admin = false;

    @Column(name = "reset_token", length = 255)
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Order> donHang;

    public User() {
    }

    public User(int id, String email, String password, String fullname, String phoneNumber, String photo, Boolean activated, Boolean admin, String resetToken, LocalDateTime resetTokenExpiry) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.activated = activated;
        this.admin = admin;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }
}
