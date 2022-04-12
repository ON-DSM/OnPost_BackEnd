package com.onpost.domain.repository;

import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.comment.SubComment;
import com.onpost.domain.repository.jpa.MainCommentRepository;
import com.onpost.domain.repository.jpa.SubCommentRepository;
import com.onpost.global.error.exception.CommentNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.onpost.domain.entity.comment.QMainComment.mainComment;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final MainCommentRepository mainCommentRepository;
    private final SubCommentRepository subCommentRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public MainComment mainLeave(MainComment comment) {
        return mainCommentRepository.save(comment);
    }

    public SubComment subLeave(SubComment comment) {
        return subCommentRepository.save(comment);
    }

    public MainComment findParent(Long parentId) {
        MainComment parent = jpaQueryFactory.selectFrom(mainComment)
                .leftJoin(mainComment.subComments)
                .fetchJoin()
                .where(mainComment.id.eq(parentId))
                .fetchOne();
        check(parent);
        return parent;
    }

    private void check(Comment object) {
        if(object == null) {
            throw CommentNotFoundException.EXCEPTION;
        }
    }
}
