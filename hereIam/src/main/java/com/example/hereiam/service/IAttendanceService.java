package com.example.hereiam.service;

import java.util.List;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.Member;

public interface IAttendanceService {

    public abstract List<Member> findUserByClassId(Long classId, String qr);

    public abstract List<Member> getUserNotAttendanceYet(Long ClassId, String qr);

    public abstract Attendance insertAttendanceService(Attendance attendance);

    public abstract Attendance findExisAttendance(Long userId, Long classId, String qr);
}
