package org.example.java5.dto;

public class ProfileResponse {
    private int id;
    private String fullname;
    private String email;
    private String phoneNumber; // Thêm thông tin số điện thoại
    private String photo;       // Thêm thông tin ảnh đại diện
    private boolean isAdmin;
    private String redirect;    // Có thể dùng để trả về thông báo lỗi hoặc đường dẫn

    // Constructor
    public ProfileResponse(int id, String fullname, String email, String phoneNumber, String photo, boolean isAdmin, String redirect) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.isAdmin = isAdmin;
        this.redirect = redirect;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}