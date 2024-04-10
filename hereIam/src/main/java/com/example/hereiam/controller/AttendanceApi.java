package com.example.hereiam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.Session;
import com.example.hereiam.entity.User;
import com.example.hereiam.mapping.AttendanceRequest;
import com.example.hereiam.mapping.ExcelExport;
import com.example.hereiam.mapping.Response;
import com.example.hereiam.mapping.Statistics;
import com.example.hereiam.service.IAttendanceService;
import com.example.hereiam.service.ISessionService;
import com.example.hereiam.service.IUserService;

@Controller
@RequestMapping("")
public class AttendanceApi {

    private static final double MAX_DISTANCE = 500;

    @Autowired
    private IAttendanceService iAttendanceService;
    @Autowired
    private ISessionService iSessionService;
    @Autowired
    private IUserService iUserService;

    @GetMapping("/attendance/{classId}")
    public ResponseEntity<?> findAllByClassroomId(@PathVariable Long classId) {
        System.err.println(classId + "classId");
        List<Statistics> statistics = iAttendanceService.findAllByClassroomId(classId);
        return ResponseEntity.ok().body(statistics);
    }

    @GetMapping("attendance-excel/{classId}")
    public ResponseEntity<?> findAllAttendanceAllSessionByClassroomId(@PathVariable Long classId) {
        List<ExcelExport> excelExports = iAttendanceService.statisticAttendanceAllSession(classId);
        return ResponseEntity.ok().body(excelExports);
    }

    @PostMapping("/attendanced")
    public ResponseEntity<?> findAllUserAttendanced(@RequestBody AttendanceRequest attendanceRequest) {
        return ResponseEntity.ok()
                .body(iAttendanceService.findUserByClassId(attendanceRequest.getClassId(), attendanceRequest.getQr()));
    }

    @PostMapping("/no-attendance-yet")
    public ResponseEntity<?> findAllUserNoAttendanceYet(@RequestBody AttendanceRequest attendanceRequest) {
        System.err.println(attendanceRequest.getQr());
        return ResponseEntity.ok().body(
                iAttendanceService.getUserNotAttendanceYet(attendanceRequest.getClassId(), attendanceRequest.getQr()));
    }

    // master room help att
    @PostMapping("/attendance-master")
    public ResponseEntity<?> insertAttendanceMaster(@RequestBody Attendance attendance) {
        User user = iUserService.findByEmailAndIdentifierInClass(attendance.getUser().getEmail(),
                attendance.getUser().getIdentifier(), attendance.getClassroom().getId());

        if (user == null) {
            return ResponseEntity.ok().body(new Response("User doesn't exist in class"));
        }

        Attendance attendanceExist = iAttendanceService.findExisAttendance(user.getId(),
                attendance.getClassroom().getId(), attendance.getQr());

        if (attendanceExist != null) {
            return ResponseEntity.ok().body(new Response("User has completed attendance"));
        }

        attendance.setUser(user);
        Attendance attendanceReturn = iAttendanceService.insertAttendanceService(attendance);
        return attendanceReturn != null ? ResponseEntity.ok().body(new Response("Attendance succcessfully"))
                : ResponseEntity.ok().body(new Response("Attendance failed, check internet and try again!"));
    }

    // user attendance
    @PostMapping("/attendance/{latitude}/{longitude}")
    public ResponseEntity<?> insertAttendance(@RequestBody Attendance attendance, @PathVariable double latitude,
            @PathVariable double longitude) {
        System.err.println("lat: " + latitude + "   " + "long: " + longitude);
        User user = iUserService.findByEmailAndIdentifierInClass(attendance.getUser().getEmail(),
                attendance.getUser().getIdentifier(), attendance.getClassroom().getId());
        System.err.println(attendance.getUser().toString());
        if (user == null) {
            return ResponseEntity.ok().body(new Response("User doesn't exist in class"));
        }
        boolean validTime = iSessionService.validTime(attendance.getQr(), attendance.getAttendanceTime());

        if (!validTime) {
            return ResponseEntity.ok().body(new Response("Attendance expires :("));
        }
        Session session = iSessionService.findSessionByQr(attendance.getQr());
        double distance = iAttendanceService.calculateDistanceInMeters(session.getLatitude(), session.getLongitude(),
                latitude, longitude);
        System.err.println(distance + "");
        if (distance > MAX_DISTANCE) {
            return ResponseEntity.ok().body(new Response("You are outside the maximum range of the class"));
        }

        if (!session.getClassroom().getId().equals(attendance.getClassroom().getId())) {
            return ResponseEntity.ok().body(new Response("You must choose the right class for you"));
        }

        Attendance attendanceExist = iAttendanceService.findExisAttendance(attendance.getUser().getId(),
                session.getClassroom().getId(), attendance.getQr());

        if (attendanceExist != null) {
            return ResponseEntity.ok().body(new Response("You has completed attendance"));
        }

        attendance.setUser(user);
        Attendance attendanceReturn = iAttendanceService.insertAttendanceService(attendance);
        return attendanceReturn != null ? ResponseEntity.ok().body(new Response("Attendance succcessfully"))
                : ResponseEntity.ok().body(new Response("Attendance failed, check internet and try again!"));
    }

    // user attendance qr
    @PostMapping("/attendance-qr/{latitude}/{longitude}")
    public ResponseEntity<?> insertAttendanceQr(@RequestBody Attendance attendance, @PathVariable double latitude,
            @PathVariable double longitude) {
        System.err.println("qr: " + attendance.getQr());
        System.err.println("lat: " + latitude + "   " + "long: " + longitude);
        boolean validTime = iSessionService.validTime(attendance.getQr(), attendance.getAttendanceTime());
        if (!validTime) {
            return ResponseEntity.ok().body(new Response("Attendance expires :("));
        }

        Session session = iSessionService.findSessionByQr(attendance.getQr());
        double distance = iAttendanceService.calculateDistanceInMeters(session.getLatitude(), session.getLongitude(),
                latitude, longitude);

        if (distance > MAX_DISTANCE) {
            return ResponseEntity.ok().body(new Response("You are outside the maximum range of the class"));
        }

        if (!session.getClassroom().getId().equals(attendance.getClassroom().getId())) {
            return ResponseEntity.ok().body(new Response("You must choose the right class for you"));
        }

        Attendance attendanceExist = iAttendanceService.findExisAttendance(attendance.getUser().getId(),
                session.getClassroom().getId(), attendance.getQr());

        if (attendanceExist != null) {
            return ResponseEntity.ok().body(new Response("You has completed attendance"));
        }

        Attendance attendanceReturn = iAttendanceService.insertAttendanceService(attendance);
        return attendanceReturn != null ? ResponseEntity.ok().body(new Response("Attendance succcessfully"))
                : ResponseEntity.ok().body(new Response("Attendance failed, check internet and try again!"));
    }

    // check attendance user
    @PostMapping("/attendance-user")
    public ResponseEntity<?> checkAttendanceUser(@RequestBody Attendance attendance) {
        Attendance attendanceReturn = iAttendanceService
                .checkAttendanceUser(attendance.getUser().getId(), attendance.getClassroom().getId(),
                        attendance.getQr());
        return attendanceReturn != null ? ResponseEntity.ok().body(new Response("checked"))
                : ResponseEntity.notFound().build();
    }

}
