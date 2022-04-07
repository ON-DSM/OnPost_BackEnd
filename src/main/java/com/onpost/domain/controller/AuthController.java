package com.onpost.domain.controller;

import com.onpost.domain.dto.auth.LoginDto;
import com.onpost.domain.dto.auth.SignupDto;
import com.onpost.domain.dto.auth.TokenDto;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Member signup(@Valid @RequestBody SignupDto signupDto) {
        return authService.signupMember(signupDto);
    }

    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginDto loginDto) {
        return authService.loginMember(loginDto);
    }

    @PatchMapping("/token")
    public TokenDto refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return authService.refreshRequest(refreshToken);
    }
}
