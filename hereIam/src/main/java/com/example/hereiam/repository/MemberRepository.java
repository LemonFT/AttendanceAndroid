package com.example.hereiam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Member;

import jakarta.transaction.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.classroom.id = :classId AND m.role = 2 AND m.status = 1")
    public abstract List<Member> findAllByClassroomId(@Param("classId") Long classId);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query("DELETE FROM Member m WHERE m.classroom.id = :classId")
    public abstract void deleteByClassroomId(Long classId);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("UPDATE Member m SET m.status = 0 WHERE m.classroom.id = :classroomId AND m.user.id = :userId")
    public abstract void outClass(Long classroomId, Long userId);

    @Modifying
    @Transactional(rollbackOn = Exception.class)
    @Query("UPDATE Member m SET m.status = 1 WHERE m.classroom.id = :classroomId AND m.user.id = :userId")
    public abstract void joinClassAgain(Long classroomId, Long userId);

    @Query("SELECT m FROM Member m WHERE m.classroom.id = :classId AND m.user.id = :userId AND m.status = 1")
    public abstract Member findAllByClassroomIdAndUserId(@Param("classId") Long classId, @Param("userId") Long userId);
}
