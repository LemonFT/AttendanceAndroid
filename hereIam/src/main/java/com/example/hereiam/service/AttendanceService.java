package com.example.hereiam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.Member;
import com.example.hereiam.entity.Session;
import com.example.hereiam.mapping.Attendances;
import com.example.hereiam.mapping.ExcelExport;
import com.example.hereiam.mapping.Statistics;
import com.example.hereiam.repository.AttendanceRepository;
import com.example.hereiam.repository.SessionRepository;

@Service
public class AttendanceService implements IAttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public List<Member> findUserByClassId(Long classId, String qr) {
        return attendanceRepository.findMembersAttendanced(classId, qr);
    }

    @Override
    public List<Member> getUserNotAttendanceYet(Long classId, String qr) {
        return attendanceRepository.findMembersNotInAttendance(classId, qr);
    }

    @SuppressWarnings("null")
    @Override
    public Attendance insertAttendanceService(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance findExisAttendance(Long userId, Long classId, String qr) {
        return attendanceRepository.findExistAttendance(userId, classId, qr);
    }

    @Override
    public Attendance checkAttendanceUser(Long userId, Long classId, String qr) {
        return attendanceRepository.checkAttendanceUser(userId, classId, qr);
    }

    @Override
    public double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusInMeters = 6371000;
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        double deltaLat = radLat2 - radLat1;
        double deltaLon = radLon2 - radLon1;

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusInMeters * c;
    }

    @Override
    public List<Statistics> findAllByClassroomId(Long classId) {
        return attendanceRepository.findAllByClassroomId(classId);
    }

    @Override
    public List<ExcelExport> statisticAttendanceAllSession(Long classId) {
        List<ExcelExport> excelExports = new ArrayList<>();
        List<Session> sessions = sessionRepository.findAllByClassroomId(classId);
        for (Session s : sessions) {
            List<Attendances> attendances = attendanceRepository.findAllAttendanceByQr(s.getQr(), classId);
            excelExports.add(new ExcelExport(s, attendances));
        }
        return excelExports;
    }
}
