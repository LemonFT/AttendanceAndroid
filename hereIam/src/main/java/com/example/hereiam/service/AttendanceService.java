package com.example.hereiam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.Member;
import com.example.hereiam.repository.AttendanceRepository;

@Service
public class AttendanceService implements IAttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

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
}
