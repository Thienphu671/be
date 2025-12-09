package org.example.java5.beans;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationBean {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 50, message = "Email không được quá 50 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 255, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 60, message = "Họ tên không được quá 60 ký tự")
    private String fullname;


    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(min = 10, max = 15, message = "Số điện thoại phải từ 10 đến 15 ký tự")
    @Pattern(regexp = "^[0-9\\-\\+]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    private String photo;

    private Boolean activated = true; // Mặc định tài khoản được kích hoạt
    private Boolean admin = false;    // Mặc định tài khoản không phải admin
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

    public @NotBlank(message = "Số điện thoại không được để trống") @Size(min = 10, max = 15, message = "Số điện thoại phải từ 10 đến 15 ký tự") @Pattern(regexp = "^[0-9\\-\\+]{10,15}$", message = "Số điện thoại không hợp lệ") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Số điện thoại không được để trống") @Size(min = 10, max = 15, message = "Số điện thoại phải từ 10 đến 15 ký tự") @Pattern(regexp = "^[0-9\\-\\+]{10,15}$", message = "Số điện thoại không hợp lệ") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

