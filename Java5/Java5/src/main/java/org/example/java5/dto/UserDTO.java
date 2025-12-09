package org.example.java5.dto;

import org.example.java5.entity.User;

public class UserDTO {
    private String email;
    private String fullname;
    private String phoneNumber;
    private String photo;
    private Boolean activated;

    // Constructor truyền vào các tham số
    public UserDTO(String email, String fullname, String phoneNumber, String photo, Boolean activated) {
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.activated = activated;
    }

    // Constructor nhận đối tượng User
    public UserDTO(User user) {
        this.email = user.getEmail();
        this.fullname = user.getFullname();
        this.phoneNumber = user.getPhoneNumber();
        this.photo = user.getPhoto();  // Giả sử User có phương thức getPhoto() trả về đường dẫn ảnh người dùng
        this.activated = user.getActivated();
    }

    // Getter và Setter cho các thuộc tính
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
