package com.onpost.domain.controller;

import com.onpost.domain.dto.LikeDto;
import com.onpost.domain.dto.post.PostCreateRequest;
import com.onpost.domain.dto.post.PostEditRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.service.ImageService;
import com.onpost.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
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
    public PostView show(@RequestParam Long id) {
        return postService.showPost(id);
    }

    @GetMapping("/main")
    public List<PostResponse> mainPage(@RequestParam(defaultValue = "LIKE") Sort sort,
                                       @RequestParam(defaultValue = "1") @Positive(message = "페이지 값이 음수입니다.") Long page) {
        return postService.pagePost(sort, page);
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
        return imageService.addImage(image, "static");
    }
}
