package com.onpost.domain.repository;

import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.Impl.CustomMemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Optional<Member> findByEmail(String email);
}
