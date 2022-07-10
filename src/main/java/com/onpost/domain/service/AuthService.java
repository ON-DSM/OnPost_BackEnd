package com.onpost.domain.service;

import com.onpost.domain.dto.auth.LoginDto;
import com.onpost.domain.dto.auth.SignupDto;
import com.onpost.domain.dto.auth.TokenDto;
import com.onpost.domain.entity.member.Authority;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.entity.member.RefreshToken;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.repository.MemberRepository;
import com.onpost.domain.repository.RefreshRepository;
import com.onpost.global.error.exception.EmailAlreadyExistsException;
import com.onpost.global.error.exception.ExpiredRefreshTokenException;
import com.onpost.global.error.exception.PasswordNotMatchException;
import com.onpost.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberFacade memberFacade;
    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;

    public static final String DEFAULT_IMAGE = System.getenv("MEMBER_DEFAULT");

    public void signupMember(SignupDto signupDto) {

        if (memberRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw EmailAlreadyExistsException.EXCEPTION;
        }

        Member member = Member.builder()
                .author(Authority.MEMBER)
                .email(signupDto.getEmail())
                .name(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .profile(DEFAULT_IMAGE)
                .build();

        memberRepository.save(member);
    }

    public TokenDto loginMember(LoginDto loginDto) {

        Member member = memberFacade.getMemberByEmail(loginDto.getEmail());

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw PasswordNotMatchException.EXCEPTION;
        }

        String access = jwtProvider.generateAccessToken(member.getAuthor().name(), loginDto.getEmail());
        String refresh = jwtProvider.generateRefreshToken(member.getAuthor().name(), loginDto.getEmail());

        TokenDto tokenDto = new TokenDto(access, refresh);
        return tokenDto;
    }

    public TokenDto refreshRequest(String token) {
        RefreshToken refreshToken = refreshRepository.findByRefreshToken(token)
                .orElseThrow(() -> ExpiredRefreshTokenException.EXCEPTION);

        String refresh = jwtProvider.generateRefreshToken(refreshToken.getAuthority(), refreshToken.getEmail());
        refreshToken.updateToken(refresh);

        String access = jwtProvider.generateAccessToken(refreshToken.getAuthority(), refreshToken.getEmail());
        return new TokenDto(access, refresh);
    }
}
