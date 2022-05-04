package com.onpost.domain.facade;

import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.PostRepository;
import com.onpost.global.error.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostFacade {

    private final PostRepository postRepository;

    public Post getPostWithAll(Long id) {
        return postRepository.findOneWithAll(id).orElseThrow(() -> PostNotFoundException.EXCEPTION);
    }

    public Post getPostWithComment(Long id) {
        return postRepository.findOneWithComment(id).orElseThrow(() -> PostNotFoundException.EXCEPTION);
    }

    public Post getPostWithWriterAndImagesAndLike(Long id) {
        return postRepository.findOneWithWriterAndImagesAndLike(id).orElseThrow(() -> PostNotFoundException.EXCEPTION);
    }

    public Post getPostWithLike(Long id) {
        return postRepository.findOneWithLike(id).orElseThrow(() -> PostNotFoundException.EXCEPTION);
    }

    public Post getPostWithImages(Long id) {
        return postRepository.findOneWithImages(id).orElseThrow(() -> PostNotFoundException.EXCEPTION);
    }
}
