package com.onpost.domain.controller;

import com.onpost.domain.dto.post.PostDto;
import com.onpost.domain.entity.Post;
import com.onpost.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public Post createPost(@Valid @RequestBody PostDto postDto) {
        return postService.createPost(postDto);
    }
}
