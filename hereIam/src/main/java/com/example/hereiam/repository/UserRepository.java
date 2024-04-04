package com.example.hereiam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public abstract User findByEmail(String email);
}