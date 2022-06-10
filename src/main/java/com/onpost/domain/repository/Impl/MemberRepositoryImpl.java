package com.onpost.domain.repository.impl;

import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.dto.member.QMemberView;
import com.onpost.domain.entity.member.QMember;
import com.onpost.domain.repository.custom.CustomMemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.onpost.domain.entity.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final static QMember follow = new QMember("member2");

    @Override
    public List<MemberView> searchFollower(String email) {
        return jpaQueryFactory.select(new QMemberView(
                follow.email, follow.name, follow.introduce, follow.profile
        )).from(member).join(member.follower, follow).where(member.email.eq(email)).fetch();
    }

    @Override
    public List<MemberView> searchFollowing(String email) {
        return jpaQueryFactory.select(new QMemberView(
                follow.email, follow.name, follow.introduce, follow.profile
        )).from(member).join(member.following, follow).where(member.email.eq(email)).fetch();
    }
}
