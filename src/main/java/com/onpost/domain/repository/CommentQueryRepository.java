package com.onpost.domain.repository;

import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.repository.jpa.CommentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final CommentRepository<MainComment> mainCommentRepository;
    private final CommentRepository<Comment> subCommentCommentRepository;
    private final PostQueryRepository postQueryRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public MainComment mainLeave(MainComment comment) {
        return mainCommentRepository.save(comment);
    }

    public Comment leave(Comment comment) {
        return subCommentCommentRepository.save(comment);
    }
}
