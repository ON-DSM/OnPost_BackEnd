package com.onpost.domain.dto.comment;

import com.onpost.domain.entity.comment.MainComment;
import lombok.Getter;

@Getter
public class MainCommentResponse extends CommentView {

    private final Integer moreComment;

    public MainCommentResponse(MainComment comment) {
        super(comment);
        this.moreComment = comment.getSubComments().size();
    }
}
