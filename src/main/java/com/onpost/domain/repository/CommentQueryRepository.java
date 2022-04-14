package com.onpost.domain.repository;

import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.repository.jpa.CommentRepository;
import com.onpost.global.error.exception.CommentNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.onpost.domain.entity.comment.QMainComment.mainComment;
import static com.onpost.domain.entity.comment.QComment.comment;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final CommentRepository commentRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Comment leave(Comment comment) {
        return commentRepository.save(comment);
    }

    public MainComment findMain(Long parentId) {
        MainComment parent = jpaQueryFactory.selectFrom(mainComment)
                .leftJoin(mainComment.subComments)
                .fetchJoin()
                .where(mainComment.id.eq(parentId))
                .fetchOne();
        check(parent);
        return parent;
    }

    public Comment findComment(Long id) {
        Comment find = jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.writer)
                .fetchJoin()
                .where(comment.id.eq(id))
                .fetchOne();
        check(find);
        return find;
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    private void check(Comment object) {
        if(object == null) {
            throw CommentNotFoundException.EXCEPTION;
        }
    }
}
