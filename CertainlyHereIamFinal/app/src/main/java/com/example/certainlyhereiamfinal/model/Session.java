package com.example.certainlyhereiamfinal.model;

import java.util.Date;

public class Session {
    private Long id;
    private String room;
    private Date dateInit;
    private Date timeEnd;
    private Classroom classroom;
    private String qr;

    private double latitude;
    private double longitude;
    public Session(Date timeEnd, Classroom classroom, String qr) {
        this.timeEnd = timeEnd;
        this.classroom = classroom;
        this.qr = qr;
    }

    public Session(Long id, String room, Date dateInit, Date timeEnd, Classroom classroom, String qr) {
        this.id = id;
        this.room = room;
        this.dateInit = dateInit;
        this.timeEnd = timeEnd;
        this.classroom = classroom;
        this.qr = qr;
    }

    public Session(Long id, String room, Date dateInit, Date timeEnd, Classroom classroom, String qr, double latitude, double longitude) {
        this.id = id;
        this.room = room;
        this.dateInit = dateInit;
        this.timeEnd = timeEnd;
        this.classroom = classroom;
        this.qr = qr;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Session(String room, Date dateInit, Date timeEnd, Classroom classroom) {
        this.room = room;
        this.dateInit = dateInit;
        this.timeEnd = timeEnd;
        this.classroom = classroom;
    }

    public Session(String room, Date dateInit, Date timeEnd, Classroom classroom, double latitude, double longitude) {
        this.room = room;
        this.dateInit = dateInit;
        this.timeEnd = timeEnd;
        this.classroom = classroom;
        this.qr = qr;
        this.latitude = latitude;
        this.longitude = longitude;
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


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
