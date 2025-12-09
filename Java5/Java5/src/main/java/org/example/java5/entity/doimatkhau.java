package org.example.java5.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class doimatkhau{
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu mới không được để trống")
    private String confirmPassword;

    public @NotBlank(message = "Mật khẩu cũ không được để trống") String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank(message = "Mật khẩu cũ không được để trống") String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank(message = "Mật khẩu mới không được để trống") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "Mật khẩu mới không được để trống") String newPassword) {
        this.newPassword = newPassword;
    }

    public @NotBlank(message = "Xác nhận mật khẩu mới không được để trống") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank(message = "Xác nhận mật khẩu mới không được để trống") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
