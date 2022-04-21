package com.onpost.domain.dto.comment;

import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.comment.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentView {

    private final Long id;
    private final String context;
    private final MemberView writer;
    private final LocalDateTime createAt;

    public CommentView(Comment comment) {
        this.id = comment.getId();
        this.context = comment.getContent();
        this.createAt = comment.getCreateAt();
        this.writer = new MemberView(comment.getWriter());
    }
}
