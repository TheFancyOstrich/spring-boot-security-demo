package com.thefancyostrich.demo.security.jwt;

public class UsernameAndPasswordAuthenticationRequest {
    private String username, password;

    public UsernameAndPasswordAuthenticationRequest() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}