package com.onpost.domain.repository.custom;

import com.onpost.domain.dto.member.MemberView;

import java.util.List;

public interface CustomMemberRepository {

    void certified(String email, String certified);

    List<MemberView> searchFollower(String email);

    List<MemberView> searchFollowing(String email);
}
