package com.onpost.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CommentCreateRequest {

    @NotBlank(message = "내용이 없습니다!")
    private String context;

    @Email(message = "이메일 형식이여야 합니다.")
    private String email;

    @NotNull(message = "아이디를 찾을 수 없습니다.")
    private Long parentId;
}
