package com.onpost.domain.controller;

import com.onpost.domain.dto.comment.CommentCreateRequest;
import com.onpost.domain.dto.comment.CommentEditRequest;
import com.onpost.domain.dto.comment.CommentResponse;
import com.onpost.domain.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/leave")
    public Long mainLeave(@Valid @RequestBody CommentCreateRequest commentRequest) {
        return commentService.saveMain(commentRequest);
    }

    @PostMapping("/leave/sub")
    public Long subLeave(@Valid @RequestBody CommentCreateRequest commentRequest) {
        return commentService.saveSub(commentRequest);
    }

    @GetMapping("/show")
    public CommentResponse show(@RequestParam Long id) {
        return commentService.showMain(id);
    }

    @PutMapping("/edit")
    public void edit(@Valid @RequestBody CommentEditRequest commentEditRequest) {
        commentService.editComment(commentEditRequest);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        commentService.deleteOne(id);
    }
}
