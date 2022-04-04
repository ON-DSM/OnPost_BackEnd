package com.onpost.domain.controller;

import com.onpost.domain.dto.LoginDto;
import com.onpost.domain.dto.SignupDto;
import com.onpost.domain.dto.TokenDto;
import com.onpost.domain.entity.Member;
import com.onpost.domain.service.AuthService;
import com.onpost.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto token = authService.loginMember(loginDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtProvider.header, "Bearer " + token.getAccessToken());

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
    }
}
