package com.onpost.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public abstract class PostDto {

    @NotNull(message = "게시물 Id가 없습니다!")
    private final Long Id;

    @NotBlank(message = "내용이 들어가야합니다!")
    private String context;

    @NotBlank(message = "제목이 들어가야합니다!")
    private String title;
}
