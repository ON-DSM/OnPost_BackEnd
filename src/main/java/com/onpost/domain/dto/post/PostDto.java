package com.onpost.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class PostDto {

    @NotNull(message = "Id가 없습니다!")
    private Long Id;

    @NotBlank(message = "내용이 들어가야합니다!")
    private String content;

    @NotBlank(message = "제목이 들어가야합니다!")
    private String title;

    private String Introduce;
}
