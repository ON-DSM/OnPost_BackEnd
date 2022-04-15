package com.onpost.domain.controller;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
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
    public void create(@Valid @ModelAttribute PostRequest postRequest) {
        postService.createPost(postRequest);
    }

    @GetMapping("/show")
    public PostView show(@RequestParam Long id) {
        return postService.showPost(id);
    }

    @GetMapping("/main")
    public List<PostResponse> mainPage(@RequestParam(defaultValue = "like") String sort,
                                       @RequestParam(defaultValue = "1") Long page) {
        return postService.pagePost(sort, page);
    }

    @PutMapping("/edit")
    public PostView edit(@Valid @ModelAttribute PostRequest postRequest) {
        return postService.editPost(postRequest);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        postService.deletePost(id);
    }

    @PostMapping("/like")
    public void like(@Valid @RequestBody IDValueDto idValueDto) {
        postService.like(idValueDto.getId(), idValueDto.getTargetId());
    }

    @PostMapping("/unlike")
    public void unlike(@Valid @RequestBody IDValueDto idValueDto) {
        postService.unlike(idValueDto.getId(), idValueDto.getTargetId());
    }
}
