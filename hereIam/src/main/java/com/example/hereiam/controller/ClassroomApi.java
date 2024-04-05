package com.example.hereiam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hereiam.entity.Classroom;
import com.example.hereiam.mapping.Response;
import com.example.hereiam.service.IClassroomService;
import com.example.hereiam.service.IMemberService;

@Controller
@RequestMapping("")
public class ClassroomApi {

    @Autowired
    private IClassroomService iClassroomService;
    @Autowired
    private IMemberService iMemberService;

    @GetMapping("/classrooms/{userId}")
    public ResponseEntity<?> findAllClassroomByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok().body(iClassroomService.findAllClassroomByUser(userId));
    }

    @PostMapping("/classroom")
    public ResponseEntity<?> insertClassRoom(@RequestBody Classroom classroom) {
        Classroom classroomReturn = iClassroomService.insertClassroom(classroom);
        return classroomReturn != null ? ResponseEntity.ok().body(classroomReturn)
                : ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/classroom")
    public ResponseEntity<?> updateClassroom(@RequestBody Classroom classroom) {
        Classroom classroomReturn = iClassroomService.updateClassroom(classroom);
        return classroomReturn != null ? ResponseEntity.ok().body(classroomReturn)
                : ResponseEntity.badRequest().body(null);
    }

    @DeleteMapping("/classroom/{classId}")
    public ResponseEntity<?> deleteClassroom(@PathVariable Long classId) {
        boolean result = iClassroomService.deleteClassroomById(classId);
        return result ? ResponseEntity.ok().body(new Response("200")) : ResponseEntity.notFound().build();
    }
}
