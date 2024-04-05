package com.example.hereiam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.User;
import com.example.hereiam.mapping.AttendanceRequest;
import com.example.hereiam.mapping.Response;
import com.example.hereiam.service.IAttendanceService;
import com.example.hereiam.service.ISessionService;
import com.example.hereiam.service.IUserService;

@Controller
@RequestMapping("")
public class AttendanceApi {

    @Autowired
    private IAttendanceService iAttendanceService;
    @Autowired
    private ISessionService iSessionService;
    @Autowired
    private IUserService iUserService;

    @PostMapping("/attendanced")
    public ResponseEntity<?> findAllUserAttendanced(@RequestBody AttendanceRequest attendanceRequest) {
        System.err.println(attendanceRequest.getQr());
        return ResponseEntity.ok()
                .body(iAttendanceService.findUserByClassId(attendanceRequest.getClassId(), attendanceRequest.getQr()));
    }

    @PostMapping("/no-attendance-yet")
    public ResponseEntity<?> findAllUserNoAttendanceYet(@RequestBody AttendanceRequest attendanceRequest) {
        System.err.println(attendanceRequest.getQr());
        return ResponseEntity.ok().body(
                iAttendanceService.getUserNotAttendanceYet(attendanceRequest.getClassId(), attendanceRequest.getQr()));
    }

    @PostMapping("/attendance-master")
    public ResponseEntity<?> insertAttendanceMaster(@RequestBody Attendance attendance) {
        User user = iUserService.findByEmailAndIdentifierInClass(attendance.getUser().getEmail(),
                attendance.getUser().getIdentifier(), attendance.getClassroom().getId());

        if (user == null) {
            return ResponseEntity.badRequest().body(new Response("User doesn't exist in class"));
        }

        Attendance attendanceExist = iAttendanceService.findExisAttendance(user.getId(),
                attendance.getClassroom().getId(), attendance.getQr());

        if (attendanceExist != null) {
            return ResponseEntity.badRequest().body(new Response("User has completed attendance"));
        }

        attendance.setUser(user);
        Attendance attendanceReturn = iAttendanceService.insertAttendanceService(attendance);
        return attendanceReturn != null ? ResponseEntity.ok().body("Attendance succcessfully")
                : ResponseEntity.badRequest().body("Attendance failed, check internet and try again!");
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> insertAttendance(@RequestBody Attendance attendance) {
        User user = iUserService.findByEmailAndIdentifierInClass(attendance.getUser().getEmail(),
                attendance.getUser().getIdentifier(), attendance.getClassroom().getId());

        if (user == null) {
            return ResponseEntity.badRequest().body(new Response("User doesn't exist in class"));
        }
        boolean validTime = iSessionService.validTime(attendance.getQr(), attendance.getAttendanceTime());

        if (!validTime) {
            return ResponseEntity.badRequest().body("Attendance expires :(");
        }

        Attendance attendanceExist = iAttendanceService.findExisAttendance(user.getId(),
                attendance.getClassroom().getId(), attendance.getQr());

        if (attendanceExist != null) {
            return ResponseEntity.badRequest().body(new Response("You has completed attendance"));
        }

        attendance.setUser(user);
        Attendance attendanceReturn = iAttendanceService.insertAttendanceService(attendance);
        return attendanceReturn != null ? ResponseEntity.ok().body("Attendance succcessfully")
                : ResponseEntity.badRequest().body("Attendance failed, check internet and try again!");
    }
}
