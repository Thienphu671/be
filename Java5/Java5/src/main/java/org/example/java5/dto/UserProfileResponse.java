package org.example.java5.dto;

public class UserProfileResponse {
    private String email;
    private String fullname;
    private String phoneNumber;
    private String photo;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String email, String fullname, String phoneNumber, String photo) {
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
