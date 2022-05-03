package com.onpost.domain.repository;

import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.Impl.CustomPostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
}
