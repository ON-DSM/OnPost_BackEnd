package com.onpost.global.jwt;

import com.onpost.domain.entity.AuthDetails;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import com.onpost.global.error.exception.EmailCertificationException;
import com.onpost.global.error.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public record CustomUserDetailsService(MemberRepository memberRepository) {

    public AuthDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        if(member.getCertified() != null) {
            throw EmailCertificationException.EXCEPTION;
        }

        return createUser(member.getEmail(), member);
    }

    private AuthDetails createUser(String email, Member member) {
        return new AuthDetails(
                email,
                member.getAuthor()
        );
    }
}