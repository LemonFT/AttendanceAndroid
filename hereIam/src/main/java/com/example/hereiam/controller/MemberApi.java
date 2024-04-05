package com.example.hereiam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hereiam.entity.Member;
import com.example.hereiam.mapping.Response;
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
        List<Member> members = iMemberService.findAllMemberByClass(classroomId);
        return ResponseEntity.ok().body(members);
    }

    @PutMapping("/member/{classroomId}/{userId}")
    public ResponseEntity<?> outClass(@PathVariable Long classroomId, @PathVariable Long userId) {
        boolean resultOut = iMemberService.outClass(classroomId, userId);
        return resultOut ? ResponseEntity.ok().body(new Response("200")) : ResponseEntity.notFound().build();
    }

    @PostMapping("/member/{classroomId}/{userId}")
    public ResponseEntity<?> joinClass(@PathVariable Long classroomId, @PathVariable Long userId) {
        boolean resultJoin = iMemberService.joinClass(classroomId, userId);
        return resultJoin ? ResponseEntity.ok().body(new Response("200")) : ResponseEntity.notFound().build();
    }

}
