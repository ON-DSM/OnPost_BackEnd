package com.onpost.domain.dto.post;

import com.onpost.domain.dto.member.MemberView;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String content;
    private String title;
    private String introduce;
    private MemberView writer;
    private Long like;
    private Long comments;
    private List<String> tags;
    private String profileImage;
    private LocalDateTime createAt;

    @QueryProjection
    public PostResponse(Long id, String content, String title, String introduce, String profile, Long comments, Long like, MemberView memberView, LocalDateTime createAt, String tags) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.introduce = introduce;
        this.comments = comments;
        this.like = like;
        this.writer = memberView;
        this.createAt = createAt;
        this.profileImage = profile;
        this.tags = List.of(tags.split(","));
    }
}
