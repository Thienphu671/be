package org.example.java5.beans;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.java5.entity.Category;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductBean {
    private int id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 250, message = "Tên sản phẩm không được quá 250 ký tự")
    private String name;

    @Size(max = 250, message = "Đường dẫn hình ảnh không được quá 250 ký tự")
    private String image;

    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private int price;

    @NotNull(message = "Ngày tạo không được để trống")
    private Date createdDate;

    @NotNull(message = "Trạng thái còn hàng không được để trống")
    private Boolean inStock;

    @NotNull(message = "Danh mục sản phẩm không được để trống")
    private Category category;
}
