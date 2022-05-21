package com.onpost.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class LikeDto {

    @Email(message = "이메일이 없습니다.")
    private String email;

    @NotNull(message = "게시물 Id가 없습니다.")
    private Long postId;
}
