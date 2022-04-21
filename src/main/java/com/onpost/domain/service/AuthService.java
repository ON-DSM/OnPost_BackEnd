package com.onpost.domain.service;

import com.onpost.domain.dto.auth.LoginDto;
import com.onpost.domain.dto.auth.LoginResponse;
import com.onpost.domain.dto.auth.SignupDto;
import com.onpost.domain.dto.auth.TokenDto;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.member.Authority;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.entity.member.RefreshToken;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.jpa.RefreshRepository;
import com.onpost.global.error.exception.EmailAlreadyExistsException;
import com.onpost.global.error.exception.ExpiredRefreshTokenException;
import com.onpost.global.error.exception.PasswordNotMatchException;
import com.onpost.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberQueryRepository memberQueryRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;

    public void signupMember(SignupDto signupDto) {

        if (memberQueryRepository.checkEmail(signupDto.getEmail())) {
            throw EmailAlreadyExistsException.EXCEPTION;
        }

        Member member = Member.builder()
                .author(Authority.USER)
                .email(signupDto.getEmail())
                .name(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .certified(certifiedKey()).build();

        memberQueryRepository.save(member);
    }

    public LoginResponse loginMember(LoginDto loginDto) {

        Member member = memberQueryRepository.findOneByEmail(loginDto.getEmail());

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw PasswordNotMatchException.EXCEPTION;
        }

        String access = jwtProvider.generateAccessToken(member.getAuthor().name(), loginDto.getEmail());
        String refresh = jwtProvider.generateRefreshToken(member.getAuthor().name(), loginDto.getEmail());

        TokenDto tokenDto = new TokenDto(access, refresh);
        return new LoginResponse(tokenDto, new MemberView(member));
    }

    public TokenDto refreshRequest(String token) {
        RefreshToken refreshToken = refreshRepository.findByRefreshToken(token)
                .orElseThrow(() -> ExpiredRefreshTokenException.EXCEPTION);

        String refresh = jwtProvider.generateRefreshToken(refreshToken.getAuthority(), refreshToken.getEmail());
        refreshToken.updateToken(refresh);

        String access = jwtProvider.generateAccessToken(refreshToken.getAuthority(), refreshToken.getEmail());
        return new TokenDto(access, refresh);
    }

    private String certifiedKey() {
        return RandomString.make(10);
    }
}
