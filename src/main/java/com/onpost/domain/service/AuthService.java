package com.onpost.domain.service;

import com.onpost.domain.dto.LoginDto;
import com.onpost.domain.dto.SignupDto;
import com.onpost.domain.dto.TokenDto;
import com.onpost.domain.entity.Authority;
import com.onpost.domain.entity.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.global.error.exception.EmailAlreadyExistsException;
import com.onpost.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberQueryRepository memberQueryRepository;
    private final PasswordEncoder passwordEncoder;

    public Member signupMember(SignupDto signupDto) {

        if (memberQueryRepository.checkEmail(signupDto.getEmail())) {
            throw EmailAlreadyExistsException.EXCEPTION;
        }

        Member.MemberBuilder builder = Member.builder()
                .email(signupDto.getEmail())
                .name(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .validMember(false);

        if (signupDto.getEmail().equals("khcho0125@dsm.hs.kr")) {
            builder.author(Authority.ADMIN);
        } else {
            builder.author(Authority.USER);
        }

        return memberQueryRepository.create(builder.build());
    }

    public TokenDto loginMember(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String authorites = jwtProvider.getAuthorities(authentication);

        return jwtProvider.generateToken(loginDto.getEmail(), authorites);
    }


}
