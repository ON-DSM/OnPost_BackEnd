package com.onpost.domain.repository.impl;

import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.dto.comment.QMainCommentResponse;
import com.onpost.domain.dto.member.QMemberView;
import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.custom.CustomCommentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.onpost.domain.entity.comment.QMainComment.mainComment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MainCommentResponse> findMainByPost(Post post) {
        return jpaQueryFactory.selectDistinct(new QMainCommentResponse(
                mainComment.id,
                mainComment.content,
                new QMemberView(mainComment.writer.email,
                        mainComment.writer.name,
                        mainComment.writer.introduce,
                        mainComment.writer.profile),
                mainComment.createAt,
                mainComment.subComments.size()
        )).from(mainComment).where(mainComment.parent_post.eq(post)).orderBy(mainComment.subComments.size().desc()).fetch();
    }
}
