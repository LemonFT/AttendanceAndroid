package com.example.hereiam.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Session;

import jakarta.transaction.Transactional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    public abstract List<Session> findAllByClassroomId(Long classroomId);

    @Query("SELECT s.timeEnd FROM Session s WHERE s.qr = :qr")
    Timestamp findTimeEndByQr(@Param("qr") String qr);

    @Transactional
    @Modifying
    @Query("DELETE FROM Session s WHERE s.classroom.id = :classId")
    public abstract void deleteByClassroomId(Long classId);

    public abstract Session findByQr(String qr);
}
