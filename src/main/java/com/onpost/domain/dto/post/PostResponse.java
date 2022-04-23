package com.onpost.domain.dto.post;

import com.onpost.domain.dto.member.MemberView;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse extends PostDto {

    private final MemberView writer;
    private final Long like;
    private final Long comments;
    private final List<String> tags;
    private final String profileImage;
    private final LocalDateTime createAt;

    @QueryProjection
    public PostResponse(Long id, String content, String title, String introduce, String profile, Long comments, Long like, MemberView memberView, LocalDateTime createAt, String tags) {
        super(id, content, title, introduce);
        this.comments = comments;
        this.like = like;
        this.writer = memberView;
        this.createAt = createAt;
        this.profileImage = profile;
        this.tags = List.of(tags.split(","));
    }
}
