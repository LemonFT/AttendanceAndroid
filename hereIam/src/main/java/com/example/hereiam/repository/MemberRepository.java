package com.example.hereiam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Member;

import jakarta.transaction.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public abstract List<Member> findAllByClassroomId(Long classId);

    // public abstract List<Member> findAllByUserId(Long userId);

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
}
