package org.example.java5.beans;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.java5.entity.Product;
import org.example.java5.entity.User;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoriteBean {
    private int id;

    @NotNull(message = "Người dùng không được để trống")
    private User user;

    @NotNull(message = "Sản phẩm không được để trống")
    private Product product;

    @NotNull(message = "Ngày thích không được để trống")
    private Date likedDate;
}
