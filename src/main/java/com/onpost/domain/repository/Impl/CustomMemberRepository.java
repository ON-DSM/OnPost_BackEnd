package com.onpost.domain.repository.Impl;

import com.onpost.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMemberRepository {

    Member findMember(Long id);
    Member findOneWithAll(Long id);
    Member findOneWithFollow(Long id);
    Member findOneByEmail(String email);
    Member findOneWithPost(Long id);
    void certified(String email, String certified);

}
