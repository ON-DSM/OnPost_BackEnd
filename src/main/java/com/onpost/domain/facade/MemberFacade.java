package com.onpost.domain.facade;

import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberRepository;
import com.onpost.global.error.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberFacade {

    private final MemberRepository memberRepository;

    public String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Member getInfoMember() {
        return getMemberByEmail(getEmail());
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithPost(String email) {
        return memberRepository.findMemberWithPost(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithFollower(String email) {
        return memberRepository.findMemberWithFollower(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithFollowing(String email) {
        return memberRepository.findMemberWithFollowing(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithAll(String email) {
        return memberRepository.findMemberWithAll(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberProfile(String email) {
        return memberRepository.findMemberProfile(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }
}
