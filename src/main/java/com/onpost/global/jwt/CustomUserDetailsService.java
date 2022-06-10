package com.onpost.global.jwt;

import com.onpost.domain.entity.AuthDetails;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public record CustomUserDetailsService(MemberFacade memberFacade) {

    public AuthDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberFacade.getMemberByEmail(email);

        return createUser(member.getEmail(), member);
    }

    private AuthDetails createUser(String email, Member member) {
        return new AuthDetails(
                email,
                member.getAuthor()
        );
    }
}