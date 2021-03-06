package com.onpost.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long like;
    private String tags;
    private List<MainCommentResponse> comments;
    private boolean doLike;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime createAt;
}
