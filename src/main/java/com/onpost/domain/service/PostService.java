package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.repository.PostQueryRepository;
import com.onpost.global.error.exception.PageSortException;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.onpost.domain.entity.QPost.post;

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

    public List<PostResponse> pagePost(String sort, Long page) {
        OrderSpecifier<?> orderBy;
        switch (sort) {
            case "new":
                orderBy = post.id.desc();
                break;
            case "like":
                orderBy = post.postLike.size().desc();
                break;
            default:
                throw PageSortException.EXCEPTION;
        }
        return postQueryRepository.showPost(orderBy, page);
    }


}
