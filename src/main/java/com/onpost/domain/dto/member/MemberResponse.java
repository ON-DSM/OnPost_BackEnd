package com.onpost.domain.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberResponse {

    private String email;
    private String name;
    private String introduce;
    private boolean visibility;
    private String profile;
    private LocalDateTime createAt;
    private Integer follower;
    private Integer following;

    @Builder
    public MemberResponse(String email, String profile,
                          LocalDateTime createAt,
                          Integer follower,
                          Integer following,
                          boolean visibility,
                          String name, String introduce) {
        this.email = email;
        this.name = name;
        this.visibility = visibility;
        this.introduce = introduce;
        this.profile = profile;
        this.createAt = createAt;
        this.follower = follower;
        this.following = following;
    }
}
