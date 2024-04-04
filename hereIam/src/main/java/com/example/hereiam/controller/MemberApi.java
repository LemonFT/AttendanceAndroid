package com.example.hereiam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hereiam.entity.Member;
import com.example.hereiam.service.IMemberService;

@Controller
@RequestMapping("")
public class MemberApi {

    @Autowired
    IMemberService iMemberService;

    @GetMapping("/members-am/{classroomId}")
    public ResponseEntity<?> countTotalMember(@PathVariable Long classroomId) {
        List<Member> members = iMemberService.findAllMemberByClass(classroomId);
        return ResponseEntity.ok().body(members.size());
    }

    @GetMapping("/members/{classroomId}")
    public ResponseEntity<?> findAllMemberInClass(@PathVariable Long classroomId) {
        System.err.println(classroomId + "");
        List<Member> members = iMemberService.findAllMemberByClass(classroomId);
        return ResponseEntity.ok().body(members);
    }
}
