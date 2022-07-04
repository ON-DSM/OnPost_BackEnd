package com.onpost.domain.controller;

import com.onpost.domain.dto.LikeDto;
import com.onpost.domain.dto.post.*;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.service.ImageService;
import com.onpost.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @PostMapping("/create")
    public void create(@Valid @ModelAttribute PostCreateRequest request) {
        postService.createPost(request);
    }

    @GetMapping("/search")
    public List<PostResponse> searchPage(@RequestParam String param) {
        return postService.search(param);
    }

    @GetMapping("/show")
    public PostView show(@RequestParam Long id, @RequestParam @Nullable String email) {
        return postService.showPost(id, email);
    }

    @GetMapping("/main")
    public List<PostResponse> mainPage(@RequestParam(defaultValue = "LIKE") Sort sort,
                                       @RequestParam(defaultValue = "1") @Positive(message = "페이지 값이 음수입니다.") Long page) {
        return postService.pagePost(sort, page);
    }

    @GetMapping("/top3")
    public List<PostTop3Response> top3(@RequestParam Sort sort) {
        return postService.top3Post(sort);
    }

    @PutMapping("/edit")
    public void edit(@ModelAttribute @Valid PostEditRequest postRequest) {
        postService.editPost(postRequest);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        postService.deletePost(id);
    }

    @PostMapping("/like")
    public void like(@Valid @RequestBody LikeDto likeDto) {
        postService.like(likeDto);
    }

    @PostMapping("/unlike")
    public void unlike(@Valid @RequestBody LikeDto likeDto) {
        postService.unlike(likeDto);
    }

    @PostMapping("/posts")
    public List<PostResponse> hasPosts(@RequestParam String email) {
        return postService.memberPosts(email);
    }

    @PostMapping("/save/image")
    public String getImage(@RequestParam MultipartFile image) {
        return imageService.getPath(image, "static");
    }
}
