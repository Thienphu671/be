package org.example.java5.dto;

public class RegisterResponse {
    private String status;    // Trạng thái: "success" hoặc "error"
    private String message;   // Thông báo kết quả
    private UserData user;    // Thông tin người dùng (nếu thành công)

    // Lớp nội bộ để chứa thông tin user
    public static class UserData {
        private int id;
        private String fullname;
        private String email;
        private String phoneNumber;

        public UserData(int id, String fullname, String email, String phoneNumber) {
            this.id = id;
            this.fullname = fullname;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        // Getters và Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    // Constructor cho trường hợp thành công
    public RegisterResponse(String status, String message,  UserData user) {
        this.status = status;
        this.message = message;

        this.user = user;
    }

    // Constructor cho trường hợp lỗi (không cần user)
    public RegisterResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.user = null;
    }

    // Getters và Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}