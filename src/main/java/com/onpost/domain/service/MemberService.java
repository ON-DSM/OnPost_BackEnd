package com.onpost.domain.service;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.repository.MemberRepository;
import com.onpost.global.annotation.ServiceSetting;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ServiceSetting
@RequiredArgsConstructor
public class MemberService {

    private final MemberFacade memberFacade;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PostService postService;
    private final CommentService commentService;

    public void editMember(MemberRequest memberRequest) {
        Member member = memberFacade.getMember(memberRequest.getId());

        if (memberRequest.getName() != null) {
            member.setName(memberRequest.getName());
        }

        if (memberRequest.getIntroduce() != null) {
            member.setIntroduce(memberRequest.getIntroduce());
        }

        if (memberRequest.getProfile() != null) {
            if (member.getProfile() != null) {
                imageService.deletePath(member.getProfile());
            }
            member.setProfile(imageService.getPath(memberRequest.getProfile(), "profile"));
        }

        memberRepository.save(member);
    }

    public MemberResponse showMember(Long id) {
        return new MemberResponse(memberFacade.getMemberWithAll(id));
    }

    public void followMember(IDValueDto IDValueDto, boolean positive) {
        Member me = memberFacade.getMemberWithFollower(IDValueDto.getId());
        Member follow = memberFacade.getMemberWithFollowing(IDValueDto.getTargetId());

        if (positive) {
            me.follow(follow);
        } else {
            me.unfollow(follow);
        }

        memberRepository.save(follow);
    }

    public List<MemberView> followers(Long id) {
        return memberRepository.searchFollower(id);
    }

    public List<MemberView> following(Long id) {
        return memberRepository.searchFollowing(id);
    }

    public void deleteMember(Long id) {
        Member member = memberFacade.getMemberWithAll(id);

        member.getFollowing().forEach(m -> {m.getFollower().remove(member); memberRepository.save(m);});

        if (member.getProfile() != null) {
            imageService.deletePath(member.getProfile());
        }

        member.getMakePost().forEach(post -> postService.deletePost(post.getId()));

        commentService.deleteWriter(member);

        memberRepository.delete(member);
    }

    public MemberView infoMember() {
        Member member = memberFacade.getInfoMember();
        return new MemberView(member);
    }
}
