package com.example.hereiam.controller;

import java.util.UUID;

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

import com.example.hereiam.entity.Session;
import com.example.hereiam.service.ISessionService;

import io.github.cdimascio.dotenv.Dotenv;

@Controller
@RequestMapping("")
public class SessionApi {

    @Autowired
    private ISessionService iSessionService;

    @GetMapping("/sessions/{classId}")
    public ResponseEntity<?> findAllSessionsByClassId(@PathVariable Long classId) {
        return ResponseEntity.ok().body(iSessionService.findAllSessionByClassId(classId));
    }

    @PostMapping("/session")
    public ResponseEntity<?> insertSession(@RequestBody Session session) {
        Dotenv env = Dotenv.configure().load();
        String keySecret = env.get("REACT_APP_SECRETKEY");
        session.setQr(keySecret + session.getClassroom().getId() + UUID.randomUUID());
        Session sessionReturn = iSessionService.insertSession(session);
        return sessionReturn != null
                ? ResponseEntity.ok().body(sessionReturn)
                : ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/session")
    public ResponseEntity<?> updateSession(@RequestBody Session session) {
        Session sessionReturn = iSessionService.updateSession(session);
        return sessionReturn != null ? ResponseEntity.ok().body(sessionReturn) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable Long sessionId) {
        return iSessionService.deleteSession(new Session(sessionId)) ? ResponseEntity.ok().body("200")
                : ResponseEntity.notFound().build();
    }

}
