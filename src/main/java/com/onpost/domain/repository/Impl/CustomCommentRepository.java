package com.onpost.domain.repository.Impl;

import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomCommentRepository {

    MainComment findMainById(Long id);
    Comment findOneById(Long id);
    List<MainComment> findMainByParent(Post parent);
    List<Comment> findAllByWriter(Member member);
}
