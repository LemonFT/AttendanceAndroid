package com.example.certainlyhereiamfinal.model;

import java.util.List;

public class ExcelExport {
    private Session session;
    private List<Attendances> attendances;

    public ExcelExport(Session session, List<Attendances> attendances) {
        this.session = session;
        this.attendances = attendances;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<Attendances> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendances> attendances) {
        this.attendances = attendances;
    }
}