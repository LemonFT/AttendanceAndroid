package com.example.hereiam.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String fullname;

    @Column
    private String identifier;

    @Column
    private String password;

    @Column
    private String avatar;

    @Column
    private String resetPwd;

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String email, String fullname, String identifier, String avatar) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.identifier = identifier;
        this.avatar = avatar;
    }
}