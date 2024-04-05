package com.example.hereiam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hereiam.entity.Classroom;
import com.example.hereiam.entity.Member;
import com.example.hereiam.entity.User;
import com.example.hereiam.repository.AttendanceRepository;
import com.example.hereiam.repository.ClassroomRepository;
import com.example.hereiam.repository.MemberRepository;
import com.example.hereiam.repository.RequestJoinRepository;
import com.example.hereiam.repository.SessionRepository;

import jakarta.transaction.Transactional;

@Service
public class ClassroomService implements IClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private RequestJoinRepository requestJoinRepository;

    @SuppressWarnings({ "unused", "null" })
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Classroom insertClassroom(Classroom classroom) {
        Classroom classroomReturn = classroomRepository.save(classroom);
        if (classroomReturn != null) {
            User user = new User(classroomReturn.getUser().getId());
            Classroom clr = new Classroom(classroomReturn.getId());
            Member member = memberRepository.save(new Member(null, user, clr, 1L, 1));
            if (member != null) {
                return classroomReturn;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("null")
    public Classroom updateClassroom(Classroom classroom) {
        Optional<Classroom> existingClassroom = classroomRepository.findById(classroom.getId());
        if (existingClassroom.isEmpty()) {
            return null;
        }
        return classroomRepository.save(classroom);
    }

    @Override
    public List<Classroom> findAllClassroomByUser(Long userId) {
        return classroomRepository.findAllMasterMemberByUserId(userId);
    }

    @Override
    public List<Classroom> findClassroomRoleMaster(Long userId) {
        return classroomRepository.findAllByUserId(userId);
    }

    @Override
    public Classroom findByName(String name) {
        return classroomRepository.findByName(name);
    }

    @SuppressWarnings("null")
    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean deleteClassroomById(Long classId) {
        try {
            memberRepository.deleteByClassroomId(classId);
            sessionRepository.deleteByClassroomId(classId);
            attendanceRepository.deleteByClassroomId(classId);
            requestJoinRepository.deleteByClassroomId(classId);
            classroomRepository.deleteById(classId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
