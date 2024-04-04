package com.example.certainlyhereiamfinal.entityui;

import java.util.Date;

public class UserAttendance {
    private int id;
    private String email;
    private String identifier;
    private String fullname;
    private String avatar;
    private Date date;

    public UserAttendance(int id, String email, String identifier, String fullname, String avatar, Date date) {
        this.id = id;
        this.email = email;
        this.identifier = identifier;
        this.fullname = fullname;
        this.avatar = avatar;
        this.date = date;
    }

    public UserAttendance(int id, String email, String identifier, String fullname, String avatar) {
        this.id = id;
        this.email = email;
        this.identifier = identifier;
        this.fullname = fullname;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
