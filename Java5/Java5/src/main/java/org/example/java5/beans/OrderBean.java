package org.example.java5.beans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.java5.entity.User;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderBean {
    private int id;

    @NotNull(message = "Người dùng không được để trống")
    private User user;

    @NotNull(message = "Ngày tạo không được để trống")
    private Date orderDate;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 250, message = "Địa chỉ không được quá 250 ký tự")
    private String address;

    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private int status;
}
