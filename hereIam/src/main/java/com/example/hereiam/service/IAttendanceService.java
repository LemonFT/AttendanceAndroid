package com.example.hereiam.service;

import java.util.List;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.Member;
import com.example.hereiam.mapping.ExcelExport;
import com.example.hereiam.mapping.Statistics;

public interface IAttendanceService {

    public abstract List<Member> findUserByClassId(Long classId, String qr);

    public abstract List<Member> getUserNotAttendanceYet(Long ClassId, String qr);

    public abstract Attendance insertAttendanceService(Attendance attendance);

    public abstract Attendance findExisAttendance(Long userId, Long classId, String qr);

    public abstract Attendance checkAttendanceUser(Long userId, Long classId, String qr);

    public abstract double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2);

    public abstract List<Statistics> findAllByClassroomId(Long classId);

    public abstract List<ExcelExport> statisticAttendanceAllSession(Long classId);
}
