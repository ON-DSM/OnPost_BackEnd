package com.onpost.domain.repository.jpa;

import com.onpost.domain.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository<T extends Comment> extends JpaRepository<T, Long> {
}
