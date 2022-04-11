package com.onpost.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentRequest {

    private String context;
    private Long writerId;
}
