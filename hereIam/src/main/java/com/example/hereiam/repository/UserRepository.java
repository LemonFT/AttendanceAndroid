package com.example.hereiam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hereiam.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public abstract User findByEmail(String email);

    @Query("SELECT u FROM Member m JOIN User u ON m.user.id = u.id WHERE u.email = :emailSimilar AND u.identifier = :identifierSimilar AND m.classroom.id = :classId and m.role = 2 and m.status = 1")
    public abstract User findUserByEmailOrIdentifierInClass(@Param("emailSimilar") String email,
            @Param("identifierSimilar") String identifier,
            @Param("classId") Long classId);
}