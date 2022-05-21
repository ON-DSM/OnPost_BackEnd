package com.onpost.domain.dto.comment;

import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.comment.MainComment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MainCommentResponse {

    private Long id;
    private String content;
    private MemberView writer;
    private LocalDateTime createAt;
    private Integer moreComment;

    @QueryProjection
    public MainCommentResponse(Long id, String content, MemberView writer, LocalDateTime createAt, Integer moreComment) {
        this.moreComment = moreComment;
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.createAt = createAt;
    }
}
