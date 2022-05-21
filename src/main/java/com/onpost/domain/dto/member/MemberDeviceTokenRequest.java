package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberDeviceTokenRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "디바이스 토큰이 없습니다.")
    private String device_token;
}
