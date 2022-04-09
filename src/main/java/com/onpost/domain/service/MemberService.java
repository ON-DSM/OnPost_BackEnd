package com.onpost.domain.service;

import com.onpost.domain.dto.member.FollowDto;
import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final ImageService imageService;
    private final PostQueryRepository postQueryRepository;

    public MemberView editMember(MemberRequest memberRequest) {
        Member member = memberQueryRepository.getCurrentMember(memberRequest.getId());

        if(memberRequest.getName() != null) {
            member.setName(memberRequest.getName());
        }

        if (memberRequest.getIntroduce() != null) {
            member.setIntroduce(memberRequest.getIntroduce());
        }

        if(memberRequest.getProfile() != null) {
            if(member.getProfile() != null) {
                imageService.deletePath(member.getProfile());
            }
            member.setProfile(imageService.getPath(memberRequest.getProfile(), "profile"));
        }

        memberQueryRepository.save(member);
        return new MemberView(member);
    }

    public MemberResponse showMember(Long id) {
        return new MemberResponse(memberQueryRepository.getMemberAll(id));
    }

    public void followMember(FollowDto followDto) {
        Member me = memberQueryRepository.getFollowRelation(followDto.getId());
        Member follow = memberQueryRepository.getFollowRelation(followDto.getFollowId());

        me.followMe(follow);

        memberQueryRepository.save(me);
        memberQueryRepository.save(follow);
    }

    public void deleteMember(Long id) {
        Member member = memberQueryRepository.getMemberAll(id);
        for(Member m : member.getFollowing()) {
            m.unfollowMe(member);
        }

        for(Member m : member.getFollower()) {
            member.unfollowMe(m);
        }

        for(Post p : member.getMakePost()) {
            postQueryRepository.delete(p);
        }

        imageService.deletePath(member.getProfile());
        memberQueryRepository.delete(member);
    }

    public MemberView infoMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberQueryRepository.getMemberByEmail(email);
        return new MemberView(member);
    }
}
