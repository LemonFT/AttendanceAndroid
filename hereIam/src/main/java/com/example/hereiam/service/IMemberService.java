package com.example.hereiam.service;

import java.util.List;

import com.example.hereiam.entity.Member;

public interface IMemberService {

    public abstract List<Member> findAllMemberByClass(Long classId);

    public abstract Member insertMemberForClass(Member member);

    public abstract boolean deleteMemberForClass(Member member);

    public abstract Member insertMember(Member member);

    public abstract boolean outClass(Long classId, Long userId);

    public abstract boolean joinClass(Long classId, Long userId);
}
