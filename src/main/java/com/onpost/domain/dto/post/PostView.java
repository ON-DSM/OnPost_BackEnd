package com.onpost.domain.dto.post;

import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.dto.member.MemberView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostView {

    private Long id;
    private String content;
    private String title;
    private String introduce;
    private String profile;
    private MemberView writer;
    private List<String> like;
    private String tags;
    private LocalDateTime createAt;
    private List<MainCommentResponse> comments;
}
