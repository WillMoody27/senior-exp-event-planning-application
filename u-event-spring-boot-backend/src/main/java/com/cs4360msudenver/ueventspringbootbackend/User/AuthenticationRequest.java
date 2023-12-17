package com.cs4360msudenver.ueventspringbootbackend.User;


import lombok.*;
@Data
public class AuthenticationRequest {

    private String username;
    private String password;
    // getters, setters, constructors
    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password, String jwtToken) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}