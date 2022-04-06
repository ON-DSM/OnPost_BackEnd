package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostQueryRepository postQueryRepository;

    public PostResponse createPost(PostRequest pcr) {
        return postQueryRepository.create(pcr);
    }

    public PostResponse showPost(Long id) {
        return postQueryRepository.show(id);
    }

    public PostResponse editPost(PostRequest per) {
        return postQueryRepository.editPost(per);
    }

    public List<PostResponse> pagePost(String sort, String direction, Long page) {
        return postQueryRepository.showPost(sort, direction, page);
    }


}
