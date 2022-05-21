package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class MemberVisibilityRequest {

    @Email(message = "이메일이 없습니다.")
    private String email;

    @NotNull(message = "공개/비공개 설정이 없습니다.")
    private boolean visibility;
}
