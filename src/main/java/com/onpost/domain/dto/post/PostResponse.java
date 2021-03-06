package com.onpost.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onpost.domain.dto.member.MemberView;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String introduce;
    private MemberView writer;
    private Long like;
    private Long comments;
    private String tags;
    private String profileImage;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime createAt;

    @QueryProjection
    public PostResponse(Long id, String title, String introduce, String profile, Long comments, Long like, MemberView memberView, LocalDateTime createAt, String tags) {
        this.id = id;
        this.title = title;
        this.introduce = introduce;
        this.comments = comments;
        this.like = like;
        this.writer = memberView;
        this.createAt = createAt;
        this.profileImage = profile;
        this.tags = tags;
    }
}
