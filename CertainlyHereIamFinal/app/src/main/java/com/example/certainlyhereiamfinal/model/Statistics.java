package com.example.certainlyhereiamfinal.model;


import java.util.Date;

public class Statistics {
    private String room;
    private Date init_session;
    private Long checkedInCount;

    public Statistics(String room, Date init_session, Long checkedInCount) {
        this.room = room;
        this.init_session = init_session;
        this.checkedInCount = checkedInCount;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getInit_session() {
        return init_session;
    }

    public void setInit_session(Date init_session) {
        this.init_session = init_session;
    }

    public Long getCheckedInCount() {
        return checkedInCount;
    }

    public void setCheckedInCount(Long checkedInCount) {
        this.checkedInCount = checkedInCount;
    }
}