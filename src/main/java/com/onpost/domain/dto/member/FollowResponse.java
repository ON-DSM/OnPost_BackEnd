package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class FollowResponse {

    private final List<MemberView> follower;
    private final List<MemberView> following;
}
