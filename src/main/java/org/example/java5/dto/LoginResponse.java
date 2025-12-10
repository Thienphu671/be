package org.example.java5.dto;

public class LoginResponse {
    private int id;
    private String fullname;
    private String email;
    private boolean isAdmin;

    // Constructor
    public LoginResponse(int id, String fullname, String email, boolean isAdmin ) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.isAdmin = isAdmin;
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


}