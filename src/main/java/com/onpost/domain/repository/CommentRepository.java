package com.onpost.domain.repository;

import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.repository.Impl.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
}
