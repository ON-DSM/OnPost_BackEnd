package com.onpost.domain.repository;

import com.onpost.domain.entity.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.onpost.domain.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public boolean checkEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
