package com.example.hereiam.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "qr", "classroom_id" })
})
public class Attendance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private String qr;

    @Column(name = "attendance_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy h:mm:ss a", timezone = "GMT+7")
    private Date attendanceTime;
}
