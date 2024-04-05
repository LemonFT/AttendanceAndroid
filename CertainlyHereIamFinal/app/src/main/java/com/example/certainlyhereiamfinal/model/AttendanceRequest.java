package com.example.certainlyhereiamfinal.model;

public class AttendanceRequest {
    private Long classId;
    private String qr;

    public AttendanceRequest(Long classId, String qr) {
        this.classId = classId;
        this.qr = qr;
    }
}
