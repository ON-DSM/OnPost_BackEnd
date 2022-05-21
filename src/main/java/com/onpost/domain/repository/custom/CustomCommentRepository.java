package com.onpost.domain.repository.custom;

import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.entity.Post;

import java.util.List;

public interface CustomCommentRepository {

    List<MainCommentResponse> findMainByPost(Post post);
}
