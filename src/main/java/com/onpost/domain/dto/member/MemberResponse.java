package com.onpost.domain.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MemberResponse extends MemberDto {

    private final String profile;

    @QueryProjection
    public MemberResponse(Long id, String name, String introduce, String profile) {
        super(id, name, introduce);
        this.profile = profile;
    }
}
