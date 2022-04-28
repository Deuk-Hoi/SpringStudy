package com.example.hellospiring.service;

import com.example.hellospiring.domain.Member;
import com.example.hellospiring.repository.MemberRepository;
import com.example.hellospiring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void join() {
        //given : 무언가를 주어줬을 때
        Member member = new Member();
        member.setName("Deuk");

        //when : 이것을 실행 했을 때
        Long savaId = memberService.join(member);

        //then : 결과가 이것이 나와야한다.
        Member findMember = memberService.findOne(savaId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void duplicate_join_exception(){
        //given
        Member member1 = new Member();
        member1.setName("hoi");

        Member member2 = new Member();
        member2.setName("hoi");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}