package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberPasswordRequest {

    @NotBlank(message = "기존 비밀번호는 8자 이상, 20자 이하입니다.")
    @Size(min = 8, max = 20, message = "기존 비밀번호는 8자 이상, 20자 이하입니다.")
    private String originPassword;

    @NotBlank(message = "새 비밀번호는 8자 이상, 20자 이하이여야 합니다.")
    @Size(min = 8, max = 20, message = "새 비밀번호는 8자 이상, 20자 이하이여야 합니다.")
    private String newPassword;

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
}
