package com.onpost.domain.repository.jpa;

import com.onpost.domain.entity.comment.MainComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCommentRepository extends JpaRepository<MainComment, Long> {
}
