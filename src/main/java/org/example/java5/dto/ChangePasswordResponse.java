package org.example.java5.dto;

public class ChangePasswordResponse {
    private String status;
    private String message;

    public ChangePasswordResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
