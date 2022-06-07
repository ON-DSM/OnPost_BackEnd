package com.onpost.domain.dto.member;

import com.onpost.domain.entity.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberView {

    private String profile;
    private String email;
    private String name;
    private String introduce;

    public MemberView(Member member) {
        this.profile = member.getProfile();
        this.introduce = member.getIntroduce();
        this.name = member.getName();
        this.email = member.getEmail();
    }

    @Builder
    @QueryProjection
    public MemberView(String email, String name, String introduce, String profile) {
        this.email = email;
        this.name = name;
        this.introduce = introduce;
        this.profile = profile;
    }
}
