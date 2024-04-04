package com.example.certainlyhereiamfinal.model;

import java.util.Date;

public class Session {
    private Long id;
    private String room;
    private String location;
    private Date dateInit;
    private Date timeEnd;
    private Classroom classroom;
    private String qr;

    public Session(Long id, String room, String location, Date dateInit, Date timeEnd, Classroom classroom, String qr) {
        this.id = id;
        this.room = room;
        this.location = location;
        this.dateInit = dateInit;
        this.timeEnd = timeEnd;
        this.classroom = classroom;
        this.qr = qr;
    }
    public Session(String room, String location, Date dateInit, Date timeEnd, Classroom classroom) {
        this.room = room;
        this.location = location;
        this.dateInit = dateInit;
        this.timeEnd = timeEnd;
        this.classroom = classroom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateInit() {
        return dateInit;
    }

    public void setDateInit(Date dateInit) {
        this.dateInit = dateInit;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
