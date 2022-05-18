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

    public Member getInfoMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getMemberByEmail(email);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithPost(Long id) {
        return memberRepository.findMemberWithPost(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithFollower(Long id) {
        return memberRepository.findMemberWithFollower(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithFollowing(Long id) {
        return memberRepository.findMemberWithFollowing(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberWithAll(Long id) {
        return memberRepository.findMemberWithAll(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    public Member getMemberProfile(Long id) {
        return memberRepository.findMemberProfile(id).orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }
}
