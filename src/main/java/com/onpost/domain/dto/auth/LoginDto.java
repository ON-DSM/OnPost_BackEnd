package com.onpost.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "이메일 유형이여야 합니다.")
    @Email(message = "이메일 유형이여야 합니다.")
    private String email;

    @NotBlank(message = "패스워드는 8자 이상, 20자 이하여야 합니다.")
    @Size(min = 8, max = 20, message = "패스워드는 8자 이상, 20자 이하여야 합니다.")
    private String password;
}
