package com.onpost.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CommentEditRequest {

    @NotNull(message = "아이디를 알 수 없습니다.")
    private final Long id;

    @NotBlank(message = "내용이 없습니다.")
    private final String context;
}
