package com.onpost.domain.repository;

import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import com.onpost.global.error.exception.MemberNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.onpost.domain.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class MemberQueryRepository {

    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public boolean checkEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public Member findMember(Long Id) {
        Member find = jpaQueryFactory.selectFrom(member)
                .where(member.id.eq(Id))
                .fetchOne();
        return check(find);
    }

    public void save(Member member) {
        memberRepository.save(member);
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

    public void delete(Member member) {
        memberRepository.delete(member);
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

    public void Certified(String email, String certified) {
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
