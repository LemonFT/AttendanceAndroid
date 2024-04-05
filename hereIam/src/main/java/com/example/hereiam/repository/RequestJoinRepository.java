package com.example.hereiam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.RequestJoin;

import jakarta.transaction.Transactional;

@Repository
public interface RequestJoinRepository extends JpaRepository<RequestJoin, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM RequestJoin r WHERE r.classroom.id = :classId")
    public abstract void deleteByClassroomId(Long classId);
}
