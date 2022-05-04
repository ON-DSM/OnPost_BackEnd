package com.onpost.domain.facade;

import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.repository.CommentRepository;
import com.onpost.global.error.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentFacade {

    private final CommentRepository commentRepository;

    public MainComment getMainById(Long id) {
        return commentRepository.findMainById(id).orElseThrow(() -> CommentNotFoundException.EXCEPTION);
    }

    public Comment getOneById(Long id) {
        return commentRepository.findOneById(id).orElseThrow(() -> CommentNotFoundException.EXCEPTION);
    }
}
