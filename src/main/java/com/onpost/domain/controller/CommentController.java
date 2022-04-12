package com.onpost.domain.controller;

import com.onpost.domain.dto.comment.CommentRequest;
import com.onpost.domain.dto.comment.CommentResponse;
import com.onpost.domain.dto.comment.CommentView;
import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/leave")
    public CommentView mainLeave(@Valid @RequestBody CommentRequest commentRequest) {
        return commentService.leaveComment(commentRequest);
    }

    @PostMapping("/leave/sub")
    public CommentView subLeave(@Valid @RequestBody CommentRequest commentRequest) {
        return commentService.leaveSubComment(commentRequest);
    }

    @GetMapping("/show")
    public CommentResponse show(@RequestParam Long id) {
        return commentService.showMain(id);
    }
}
