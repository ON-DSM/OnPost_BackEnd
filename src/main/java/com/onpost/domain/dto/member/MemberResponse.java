package com.onpost.domain.dto.member;

import com.onpost.domain.entity.member.Authority;
import com.onpost.domain.entity.member.Member;
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

    public MemberResponse(Member member) {
        super(member.getId(), member.getName(), member.getIntroduce());
        this.profile = member.getProfile();
        this.createAt = member.getCreateAt();
        this.authority = member.getAuthor();
        this.follower = member.getFollower().size();
        this.following = member.getFollowing().size();
        this.makePost = member.getMakePost().size();
    }
}
