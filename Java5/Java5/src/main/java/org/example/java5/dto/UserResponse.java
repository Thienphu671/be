package org.example.java5.dto;

import lombok.Data;

@Data
public class UserResponse {
    private int id;
    private String email;
    private String fullname;
    private String phoneNumber;
    private String photo;
    private Boolean activated;
    private Boolean admin;

    public UserResponse(int id, String email, String fullname, String phoneNumber, String photo, Boolean activated, Boolean admin) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.activated = activated;
        this.admin = admin;
    }
}
