package com.example.certainlyhereiamfinal.model;

public class User {
    private Long id;
    private String email;
    private String fullname;
    private String identifier;
    private String password;
    private String avatar;

    public User(Long id, String email, String fullname, String identifier, String password, String avatar) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.identifier = identifier;
        this.password = password;
        this.avatar = avatar;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String fullname, String identifier, String avatar) {
        this.id = id;
        this.fullname = fullname;
        this.identifier = identifier;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}