package com.onpost.domain.dto.comment;

import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentView {

    private Long id;
    private String content;
    private MemberView writer;
    private LocalDateTime createAt;

    public CommentView(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createAt = comment.getCreateAt();
        this.writer = new MemberView(comment.getWriter());
    }
}
