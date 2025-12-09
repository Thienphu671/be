package org.example.java5.beans;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.java5.entity.Order;
import org.example.java5.entity.Product;
import org.example.java5.entity.Order;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class donhangBean {
    private int id;

    @NotNull(message = "Đơn hàng không được để trống")
    private Order order;

    @NotNull(message = "Sản phẩm không được để trống")
    private Product product;

    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private int price;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private int quantity;
}
