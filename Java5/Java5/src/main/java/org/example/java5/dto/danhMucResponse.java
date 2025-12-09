package org.example.java5.dto;

import lombok.Data;
import org.example.java5.entity.Product;

import java.util.List;

@Data

public class danhMucResponse {
    private Integer id;
    private String ten;
    private int status = 0;


    public danhMucResponse(Integer id, String ten, int status) {
        this.id = id;
        this.ten = ten;
        this.status = status;

    }

}
