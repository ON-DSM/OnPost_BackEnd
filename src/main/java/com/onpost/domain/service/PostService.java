package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostDto;
import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;

    public Post createPost(PostDto postDto) {
        return postQueryRepository.create(postDto);
    }
}
