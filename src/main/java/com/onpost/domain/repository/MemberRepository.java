package com.onpost.domain.repository;

import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.custom.CustomMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m left join m.makePost where m.email = :email")
    Optional<Member> findMemberWithPost(@Param("email") String email);

    @Query("select m from Member m left join m.follower left join m.makePost left join m.following where m.email = :email")
    Optional<Member> findMemberWithAll(@Param("email") String email);

    @Query("select m from Member m left join m.follower left join m.following where m.email = :email")
    Optional<Member> findMemberProfile(@Param("email") String email);

    @Query("select m from Member m left join m.follower where m.email = :email")
    Optional<Member> findMemberWithFollower(@Param("email") String email);

    @Query("select m from Member m left join m.following where m.email = :email")
    Optional<Member> findMemberWithFollowing(@Param("email") String email);
}
