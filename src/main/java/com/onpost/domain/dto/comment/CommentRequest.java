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

    @NotNull(message = "작성자 아이디가 없습니다!")
    private Long writerId;

    @NotNull(message = "어디에 저장해야하는지 모르겠네요")
    private Long parentId;
}
