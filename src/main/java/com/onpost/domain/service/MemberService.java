package com.onpost.domain.service;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.member.FollowResponse;
import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.repository.MemberRepository;
import com.onpost.global.annotation.ServiceSetting;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

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
            follow.followMe(me);
        } else {
            follow.unfollowMe(me);
        }

        memberRepository.save(follow);
    }

    public FollowResponse followList(Long id) {
        Member member = memberFacade.getMemberWithFollow(id);
        return FollowResponse.builder()
                .follower(member.getFollower().stream().map(MemberView::new).collect(Collectors.toList()))
                .following(member.getFollowing().stream().map(MemberView::new).collect(Collectors.toList()))
                .build();
    }

    public void deleteMember(Long id) {
        Member member = memberFacade.getMemberWithAll(id);

        member.getFollower().forEach(m -> m.unfollowMe(member));
        member.getFollowing().forEach(member::unfollowMe);

        if (member.getProfile() != null) {
            imageService.deletePath(member.getProfile());
        }

        member.getMakePost().forEach(p -> postService.deletePost(p.getId()));

        commentService.deleteWriter(member);

        memberRepository.delete(member);
    }

    public MemberView infoMember() {
        Member member = memberFacade.getInfoMember();
        return new MemberView(member);
    }
}
