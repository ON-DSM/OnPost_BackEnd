package com.onpost.domain.repository.Impl;

import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Post;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPostRepository {

    Post findOneWithImages(Long id);
    Post findOneWithAll(Long id);
    Post findOneWithWriterAndImagesAndLike(Long id);
    List<PostResponse> findPage(OrderSpecifier<?> sort, Long page);
    Post findOneWithComment(Long id);
    Post findOneWithLike(Long id);
}
