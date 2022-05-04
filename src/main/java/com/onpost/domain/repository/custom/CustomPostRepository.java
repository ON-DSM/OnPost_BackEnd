package com.onpost.domain.repository.custom;

import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Sort;

import java.util.List;

public interface CustomPostRepository {
    List<PostResponse> findPage(Sort sort, Long page);
}
