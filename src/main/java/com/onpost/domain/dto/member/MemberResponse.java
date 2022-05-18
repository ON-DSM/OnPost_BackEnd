package com.onpost.domain.dto.member;

import com.onpost.domain.entity.member.Authority;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse extends MemberDto {

    private final String profile;
    private final Authority authority;
    private final LocalDateTime createAt;
    private final Integer follower;
    private final Integer following;

    @Builder
    public MemberResponse(String profile,
                          Authority authority,
                          LocalDateTime createAt,
                          Integer follower,
                          Integer following,
                          boolean public_profile,
                          Long id, String name, String introduce) {
        super(id, name, introduce, public_profile);
        this.profile = profile;
        this.authority = authority;
        this.createAt = createAt;
        this.follower = follower;
        this.following = following;
    }
}
