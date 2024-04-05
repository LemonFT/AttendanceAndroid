package com.example.hereiam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Attendance;
import com.example.hereiam.entity.Member;

import jakarta.transaction.Transactional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

        @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND a.classroom.id = :classId AND a.qr = :qr")
        public abstract Attendance findExistAttendance(@Param("userId") Long userId, @Param("classId") Long classId,
                        @Param("qr") String qr);

        @Query("SELECT m FROM Member m " +
                        "INNER JOIN Attendance a ON m.user.id = a.user.id " +
                        "WHERE m.classroom.id = :classId AND a.classroom.id = :classId AND a.qr = :qr")
        public abstract List<Member> findMembersAttendanced(@Param("classId") Long classId, @Param("qr") String qr);

        @Query("SELECT m " +
                        " FROM Member m WHERE m.role = 2 AND NOT EXISTS " +
                        " (SELECT a FROM Attendance a WHERE a.classroom.id = :classId " +
                        " AND m.user = a.user AND m.classroom = a.classroom AND a.qr = :qr) AND m.classroom.id = :classId")
        public abstract List<Member> findMembersNotInAttendance(@Param("classId") Long classId, @Param("qr") String qr);

        @Transactional
        @Modifying
        @Query("DELETE FROM Attendance a WHERE a.classroom.id = :classId")
        public abstract void deleteByClassroomId(Long classId);
}
