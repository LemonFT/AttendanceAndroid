package com.example.hereiam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    public abstract List<Session> findAllByClassroomId(Long classroomId);
}
