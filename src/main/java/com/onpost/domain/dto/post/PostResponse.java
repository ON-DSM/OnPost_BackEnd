package com.onpost.domain.dto.post;

import com.onpost.domain.dto.member.MemberView;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse extends PostDto {

    private final MemberView writer;
    private final Long like;
    private final Long comments;
    private final String profileImage;
    private final LocalDateTime createAt;

    public PostResponse(Long id, String content, String title, String introduce, String profile, Long comments, Long like, MemberView memberView, LocalDateTime createAt) {
        super(id, content, title, introduce);
        this.comments = comments;
        this.like = like;
        this.writer = memberView;
        this.createAt = createAt;
        this.profileImage = profile;
    }
}
