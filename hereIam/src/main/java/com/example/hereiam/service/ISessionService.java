package com.example.hereiam.service;

import java.util.List;

import com.example.hereiam.entity.Session;

public interface ISessionService {

    public abstract Session insertSession(Session session);

    public abstract Session updateSession(Session session);

    public abstract List<Session> findAllSessionByClassId(Long classId);

    public abstract boolean deleteSession(Session session);

}
