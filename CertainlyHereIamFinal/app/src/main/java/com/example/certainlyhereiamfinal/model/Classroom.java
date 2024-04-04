package com.example.certainlyhereiamfinal.model;

public class Classroom {
    private Long id;
    private String name;
    private User user;

    public Classroom(Long id) {
        this.id = id;
    }

    public Classroom(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Classroom(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
