package com.onpost.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class IDValueDto {

    @NotNull(message = "아이디를 찾을 수 없습니다.")
    private Long id;

    @NotNull(message = "아이디를 찾을 수 없습니다.")
    private Long targetId;
}
