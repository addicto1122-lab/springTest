package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public boolean insertMember(Member member){
        return memberRepository.insertMember(member);
    }

    public Member loginMember(String email, String password){
        return memberRepository.loginMember(email, password);
    }

    public Member selectMember(int id){
        return memberRepository.selectMember(id);
    }

    public Member updateMember(Member member){
        return memberRepository.updateMember(member);
    }

    public String deleteMember(int id, String password){
        Member member = memberRepository.selectMember(id);
        if (member.getPassword().equals(password)){
            boolean c = memberRepository.deleteMember(id);
            if (c){
                return "성공";
            }else{
                return "실패";
            }
        }else{
            return "비밀번호 불일치";
        }
    }
}
