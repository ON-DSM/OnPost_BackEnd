package com.onpost.domain.repository.Impl;

import com.onpost.domain.repository.custom.CustomMemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.onpost.domain.entity.member.QMember.member;

@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void certified(String email, String certified) {
        jpaQueryFactory.update(member)
                .setNull(member.certified)
                .where(member.email.eq(email).and(member.certified.eq(certified)))
                .execute();
    }
}
