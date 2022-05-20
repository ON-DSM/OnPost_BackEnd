package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberEditRequest {

    @NotBlank(message = "이름은 2자 이상, 10자 이하이여야 합니다.")
    @Size(min = 2, max = 10, message = "이름은 2자 이상, 10자 이하이여야 합니다.")
    private String name;

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    private String introduce;
    private MultipartFile profile;
}
