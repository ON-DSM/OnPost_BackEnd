package com.onpost.domain.dto.member;

import com.onpost.domain.entity.member.Authority;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse extends MemberDto {

    private final String profile;
    private final Authority authority;
    private final LocalDateTime createAt;
    private final Integer follower;
    private final Integer following;
    private final Integer makePost;

    @QueryProjection
    public MemberResponse(Long id, String name,
                          String introduce, String profile,
                          Authority authority, LocalDateTime createAt,
                          Integer follower, Integer following, Integer makePost) {
        super(id, name, introduce);
        this.profile = profile;
        this.authority = authority;
        this.createAt = createAt;
        this.follower = follower;
        this.following = following;
        this.makePost = makePost;
    }
}
