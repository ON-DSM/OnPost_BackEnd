package com.onpost.domain.repository;

import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.QMemberResponse;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.onpost.domain.entity.member.QMember.member;

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

    public Member getCurrentMember(Long Id) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.id.eq(Id))
                .fetchOne();
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public MemberResponse getView(Long id) {
        return jpaQueryFactory.query().select(new QMemberResponse(member.id,
                        member.name,
                        member.introduce,
                        member.profile,
                        member.author,
                        member.createAt,
                        member.follower.size(),
                        member.following.size(),
                        member.makePost.size()))
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    public Member getFollowRelation(Long id) {
        return jpaQueryFactory.selectFrom(member)
                .leftJoin(member)
                .fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
    }
}
