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
    public void certified(String email, String certified) {
        jpaQueryFactory.update(member)
                .setNull(member.certified)
                .where(member.email.eq(email).and(member.certified.eq(certified)))
                .execute();
    }

    @Override
    public List<MemberView> searchFollower(Long id) {
        return jpaQueryFactory.select(new QMemberView(
                follow.id, follow.name, follow.introduce, follow.profile, follow.email
        )).from(member).join(member.follower, follow).where(member.id.eq(id)).fetch();
    }

    @Override
    public List<MemberView> searchFollowing(Long id) {
        return jpaQueryFactory.select(new QMemberView(
                follow.id, follow.name, follow.introduce, follow.profile, follow.email
        )).from(member).join(member.following, follow).where(member.id.eq(id)).fetch();
    }
}
