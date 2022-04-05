package com.onpost.domain.service;

import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.entity.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryRepository memberQueryRepository;

    public Member edit(MemberRequest memberRequest) {
        Member member = memberQueryRepository.getCurrentMember(memberRequest.getId());

        if(memberRequest.getName() != null) {
            member.setName(memberRequest.getName());
        }

        if (memberRequest.getIntroduce() != null) {
            member.setIntroduce(memberRequest.getIntroduce());
        }

//        if(memberRequest.getProfile() != null) {
//            member.setProfile(memberRequest.getProfile());
//        }

        return memberQueryRepository.save(member);
    }

    public MemberResponse showMember(Long id) {
        return memberQueryRepository.getView(id);
    }
}
