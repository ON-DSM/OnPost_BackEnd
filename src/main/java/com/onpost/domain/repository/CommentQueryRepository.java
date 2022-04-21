package com.onpost.domain.repository;

import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.global.error.exception.CommentNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.onpost.domain.entity.comment.QMainComment.mainComment;
import static com.onpost.domain.entity.comment.QComment.comment;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MainComment findMainById(Long id) {
        MainComment find = jpaQueryFactory.selectFrom(mainComment)
                .leftJoin(mainComment.writer)
                .fetchJoin()
                .leftJoin(mainComment.subComments)
                .fetchJoin()
                .where(mainComment.id.eq(id))
                .fetchOne();
        check(find);
        return find;
    }

    public Comment findOneById(Long id) {
        Comment find = jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.writer)
                .fetchJoin()
                .where(comment.id.eq(id))
                .fetchOne();
        check(find);
        return find;
    }

    public List<MainComment> findMainByParent(Post parent) {
        return jpaQueryFactory.selectFrom(mainComment)
                .leftJoin(mainComment.subComments)
                .fetchJoin()
                .where(mainComment.parent_post.eq(parent))
                .fetch();
    }

    public List<Comment> findAllByWriter(Member member) {
        return jpaQueryFactory.selectFrom(comment)
                .where(comment.writer.eq(member))
                .fetch();
    }

    private void check(Comment object) {
        if(object == null) {
            throw CommentNotFoundException.EXCEPTION;
        }
    }
}
