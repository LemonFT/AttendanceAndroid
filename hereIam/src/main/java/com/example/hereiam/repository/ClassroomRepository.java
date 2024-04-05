package com.example.hereiam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    public abstract List<Classroom> findAllByUserId(Long userId);

    public abstract Classroom findByName(String name);

    @Query("SELECT c FROM Classroom c JOIN Member m ON c.id = m.classroom.id WHERE m.user.id = :userId AND m.status = 1")
    List<Classroom> findAllMasterMemberByUserId(@Param("userId") Long userId);
}
