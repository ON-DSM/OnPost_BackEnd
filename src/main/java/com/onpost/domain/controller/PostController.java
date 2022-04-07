package com.onpost.domain.controller;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public PostResponse create(@Valid @ModelAttribute PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @GetMapping("/show")
    public PostResponse show(@RequestParam Long id) {
        return postService.showPost(id);
    }

    @GetMapping("/main")
    public List<PostResponse> mainPage(@RequestParam(defaultValue = "like") String sort,
                                       @RequestParam(defaultValue = "1") Long page) {
        return postService.pagePost(sort, page);
    }

    @PutMapping("/edit")
    public PostResponse edit(@Valid @RequestBody PostRequest postRequest) {
        return postService.editPost(postRequest);
    }
}
