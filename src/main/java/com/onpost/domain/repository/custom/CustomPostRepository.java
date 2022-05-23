package com.onpost.domain.repository.custom;

import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Sort;

import java.util.List;

public interface CustomPostRepository {
    List<PostResponse> searchMainPage(Sort sort, Long page);

    List<PostResponse> searchMemberPosts(String email);

    List<PostResponse> searchPosts(String param);
}
