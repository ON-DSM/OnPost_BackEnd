package com.onpost.domain.repository.jpa;

import com.onpost.domain.entity.comment.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
}
