package com.onpost.domain.dto.member;

import com.onpost.domain.entity.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MemberView extends MemberDto {

    private final String image;
    private final String email;

    @QueryProjection
    public MemberView(Member writer) {
        super(writer.getId(), writer.getName(), writer.getIntroduce());
        this.image = writer.getProfile();
        this.email = writer.getEmail();
    }
}
