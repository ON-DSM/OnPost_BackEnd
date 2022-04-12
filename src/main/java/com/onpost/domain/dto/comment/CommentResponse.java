package com.onpost.domain.dto.comment;

import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.comment.SubComment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponse extends CommentView {

    private final List<CommentView> subComments;

    public CommentResponse(MainComment comment) {
        super(comment);
        this.subComments = comment.getSubComments().stream().map(CommentView::new).collect(Collectors.toList());
    }
}
