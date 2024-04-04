package com.example.hereiam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hereiam.entity.Member;
import com.example.hereiam.repository.MemberRepository;

@Service
public class MemberService implements IMemberService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public List<Member> findAllMemberByClass(Long classId) {
        return memberRepository.findAllByClassroomId(classId);
    }

    @SuppressWarnings("null")
    @Override
    public Member insertMemberForClass(Member member) {
        return memberRepository.save(member);
    }

    @SuppressWarnings("null")
    @Override
    public boolean deleteMemberForClass(Member member) {
        Long memberId = member.getId();
        memberRepository.delete(member);
        Member deletedMember = memberRepository.findById(memberId).orElse(null);
        return deletedMember == null;
    }

    @Override
    public List<Member> findAllMemberByUser(Long userId) {
        return memberRepository.findAllByUserId(userId);
    }

    @SuppressWarnings("null")
    @Override
    public Member insertMember(Member member) {
        return memberRepository.save(member);
    }

}
