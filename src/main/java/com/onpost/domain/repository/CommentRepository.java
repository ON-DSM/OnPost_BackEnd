package com.onpost.domain.repository;

import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.custom.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    @Query("select m from MainComment m left join m.writer left join m.subComments where m.id = :id")
    Optional<MainComment> findMainById(@Param("id") Long id);

    @Query("select c from Comment c left join c.writer where c.id = :id")
    Optional<Comment> findOneById(@Param("id") Long id);

    @Query("select m from MainComment m left join m.subComments where m.parent_post = :post")
    List<MainComment> findMainByPost(@Param("post") Post post);

    void deleteCommentByWriter(Member writer);
}
