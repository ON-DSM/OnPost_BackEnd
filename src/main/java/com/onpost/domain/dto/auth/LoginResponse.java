package com.onpost.domain.dto.auth;

import com.onpost.domain.dto.member.MemberView;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final TokenDto tokenDto;
    private final MemberView memberView;

    public LoginResponse(TokenDto tokenDto, MemberView memberView) {
        this.tokenDto = tokenDto;
        this.memberView = memberView;
    }
}
