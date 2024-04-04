package com.example.hereiam.service;

import java.util.List;

import com.example.hereiam.entity.Member;

public interface IMemberService {

    public abstract List<Member> findAllMemberByClass(Long classId);

    public abstract Member insertMemberForClass(Member member);

    public abstract boolean deleteMemberForClass(Member member);

    public abstract List<Member> findAllMemberByUser(Long userId);

    public abstract Member insertMember(Member member);
}
