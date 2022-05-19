package com.onpost.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Email(message = "이메일 형식이 아닙니다.")
    private String otherEmail;
}
