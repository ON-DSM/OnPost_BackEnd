package com.onpost.global.jwt;

import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("UserDetailsService")
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email).map(member -> createUser(email, member)).orElseThrow();
    }

    private User createUser(String email, Member member) {
        return new User(
                email, member.getPassword(),
                member.getAuthor().getAuth()
        );
    }
}