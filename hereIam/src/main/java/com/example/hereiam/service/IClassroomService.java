package com.example.hereiam.service;

import java.util.List;

import com.example.hereiam.entity.Classroom;

public interface IClassroomService {

    public abstract Classroom insertClassroom(Classroom classroom);

    public abstract Classroom findByName(String name);

    public abstract List<Classroom> findClassroomRoleMaster(Long userId);

    public abstract List<Classroom> findAllClassroomByUser(Long userId);

    public abstract Classroom updateClassroom(Classroom classroom);

    public abstract boolean deleteClassroomById(Long classId);
}
