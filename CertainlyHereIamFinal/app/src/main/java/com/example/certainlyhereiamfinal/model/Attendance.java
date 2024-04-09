package com.example.certainlyhereiamfinal.model;

import java.util.Date;

public class Attendance {
    private long id;

    private User user;

    private Classroom classroom;

    private String qr;

    private Date attendanceTime;

    public Attendance(User user, Classroom classroom, String qr) {
        this.user = user;
        this.classroom = classroom;
        this.qr = qr;
    }

    public Attendance(User user, Classroom classroom, String qr, Date attendanceTime) {
        this.user = user;
        this.classroom = classroom;
        this.qr = qr;
        this.attendanceTime = attendanceTime;
    }

    public Attendance(long id, User user, Classroom classroom, String qr, Date attendanceTime) {
        this.id = id;
        this.user = user;
        this.classroom = classroom;
        this.qr = qr;
        this.attendanceTime = attendanceTime;
    }
}
