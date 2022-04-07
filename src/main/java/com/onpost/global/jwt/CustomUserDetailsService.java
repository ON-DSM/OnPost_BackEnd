package com.onpost.global.jwt;

import com.onpost.domain.entity.AuthDetails;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import com.onpost.global.error.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final MemberRepository memberRepository;

    public AuthDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(member -> createUser(email, member)).orElseThrow(() -> MemberNotFoundException.EXCEPTION);

    }

    private AuthDetails createUser(String email, Member member) {
        return new AuthDetails(
                email,
                member.getAuthor()
        );
    }
}