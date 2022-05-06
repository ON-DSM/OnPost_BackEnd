package com.onpost.domain.repository.custom;

import com.onpost.domain.dto.member.MemberView;

import java.util.List;

public interface CustomMemberRepository {

    void certified(String email, String certified);

    List<MemberView> searchFollower(Long id);

    List<MemberView> searchFollowing(Long id);
}
