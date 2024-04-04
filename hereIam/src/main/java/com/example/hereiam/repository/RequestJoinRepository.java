package com.example.hereiam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.RequestJoin;

@Repository
public interface RequestJoinRepository extends JpaRepository<RequestJoin, Long> {

}
