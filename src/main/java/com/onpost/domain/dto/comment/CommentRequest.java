package com.onpost.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CommentRequest {

    @NotBlank(message = "내용이 없습니다!")
    private String context;

    @NotNull(message = "아이디를 찾을 수 없습니다.")
    private Long writerId;

    @NotNull(message = "아이디를 찾을 수 없습니다.")
    private Long parentId;
}
