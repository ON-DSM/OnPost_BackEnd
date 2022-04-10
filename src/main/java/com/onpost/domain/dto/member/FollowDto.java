package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class FollowDto {

    @NotNull(message = "제 아이디가 담기지 않았어요!")
    private Long id;

    @NotNull(message = "팔로우 유저의 Id가 포함되지 않았습니다!")
    private Long followId;
}
