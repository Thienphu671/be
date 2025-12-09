package org.example.java5.beans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryBean {
    private int id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 250, message = "Tên danh mục không được quá 250 ký tự")
    private String name;
}
