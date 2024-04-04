package com.example.certainlyhereiamfinal.model;

public class Member {
    private Long id;
    private User user;
    private Classroom classroom;
    private Long role;

    public Member(Long id) {
        this.id = id;
    }

    public Member(Long id, User user, Classroom classroom, Long role) {
        this.id = id;
        this.user = user;
        this.classroom = classroom;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }
}
