package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    /**
     * 회원 가입
     */
    public void join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
    }

    //중복 회원 검증
    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    //
    public Member login(Member member) {
        Optional<Member> rs = memberRepository.findById(member.getId());
        if(rs.isPresent()) {
            if(rs.get().getPassword().equals(member.getPassword()))
                return rs.get(); //로그인 성공
            else
                throw new IllegalStateException("비밀번호가 틀립니다.");
        } else
            throw new IllegalStateException("존재하지 않는 아이디입니다.");
    }
    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(String memberId) {
        return memberRepository.findById(memberId);
    }

}
