package com.onpost.domain.repository.query;

import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.Impl.CustomMemberRepository;
import com.onpost.global.error.exception.MemberNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.onpost.domain.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Member findMember(Long Id) {
        Member find = jpaQueryFactory.selectFrom(member)
                .where(member.id.eq(Id))
                .fetchOne();
        return check(find);
    }

    public Member findOneWithAll(Long id) {
        Member find = jpaQueryFactory.selectFrom(member)
                .leftJoin(member.makePost)
                .fetchJoin()
                .leftJoin(member.following)
                .fetchJoin()
                .leftJoin(member.follower)
                .fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public Member findOneWithFollow(Long id) {
        Member find = jpaQueryFactory.selectFrom(member)
                .leftJoin(member.follower)
                .fetchJoin()
                .leftJoin(member.following)
                .fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public Member findOneByEmail(String email) {
        Member find = jpaQueryFactory.selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne();
        return check(find);
    }

    public Member findOneWithPost(Long id) {
        Member find = jpaQueryFactory.selectFrom(member)
                .leftJoin(member.makePost)
                .fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public void certified(String email, String certified) {
        jpaQueryFactory.update(member)
                .setNull(member.certified)
                .where(member.email.eq(email).and(member.certified.eq(certified)))
                .execute();
    }

    private Member check(Member m) {
        if(m == null) {
            throw MemberNotFoundException.EXCEPTION;
        }
        return m;
    }
}
