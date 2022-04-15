package com.onpost.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class IDValueDto {

    @NotNull(message = "제 아이디가 담기지 않았어요!")
    private Long id;

    @NotNull(message = "타겟 유저의 Id가 포함되지 않았습니다!")
    private Long targetId;
}
