package com.example.hereiam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hereiam.entity.Session;
import com.example.hereiam.repository.SessionRepository;

@Service
public class SessionService implements ISessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @SuppressWarnings("null")
    @Override
    public Session insertSession(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public List<Session> findAllSessionByClassId(Long classId) {
        return sessionRepository.findAllByClassroomId(classId);
    }

    @SuppressWarnings("null")
    @Override
    public boolean deleteSession(Session session) {
        Long sessionId = session.getId();
        sessionRepository.delete(session);
        Optional<Session> sessionFind = sessionRepository.findById(sessionId);
        return !sessionFind.isPresent();
    }

    @SuppressWarnings("null")
    @Override
    public Session updateSession(Session session) {
        Optional<Session> sessionFind = sessionRepository.findById(session.getId());
        if (!sessionFind.isPresent()) {
            return null;
        }
        Session sessionReturn = sessionRepository.save(session);
        return sessionReturn != null ? sessionReturn : null;
    }

}
