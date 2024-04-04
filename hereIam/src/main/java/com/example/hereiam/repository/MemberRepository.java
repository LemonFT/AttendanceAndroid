package com.example.hereiam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public abstract List<Member> findAllByClassroomId(Long classId);

    public abstract List<Member> findAllByUserId(Long userId);

}
