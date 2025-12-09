package org.example.java5.beans;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class studenbean {
    public String name;
    @NotBlank(message="ho va ten khong duoc rong")
    public String email;
    @NotBlank(message="email ko dung dinh dang")

    @NotBlank(message = "SDT khong duoc rong")
    @Length(min = 10,max = 10,message = "SDT phai co 10 ky tu ")
    public String phone;
    @Min(value = 1,message = "khong duoc bo trong gioi tinh")
    public int gender;
    @NotBlank(message = "khong duoc bo trong")
    @Range(min =0,max = 10,message = "nhap tu 0-10")
    public double point;
    @Min(value = 0,message = "nganh hoc bat buoc chon")

    public int major;

}
