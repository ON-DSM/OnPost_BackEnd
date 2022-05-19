package com.onpost.domain.service;

import com.onpost.domain.dto.FollowDto;
import com.onpost.domain.dto.member.*;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.repository.MemberRepository;
import com.onpost.global.annotation.ServiceSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@ServiceSetting
@RequiredArgsConstructor
public class MemberService {

    private final MemberFacade memberFacade;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PostService postService;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder;

    public void editMember(MemberRequest request) {
        Member member = memberFacade.getMemberByEmail(request.getEmail());
        member.setName(request.getName());
        member.setIntroduce(request.getIntroduce());

        if(request.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getProfile() != null) {
            if (member.getProfile() != null) {
                imageService.deletePath(member.getProfile());
            }
            member.setProfile(imageService.getPath(request.getProfile(), "profile"));
        }

        memberRepository.save(member);
    }

    public void setVisibility(MemberVisibilityRequest request) {
        Member member = memberFacade.getMemberByEmail(request.getEmail());
        member.setVisibility(request.isVisibility());
        memberRepository.save(member);
    }

    public MemberResponse showMember(String email) {
        Member member = memberFacade.getMemberProfile(email);

        return MemberResponse.builder()
                .name(member.getName())
                .createAt(member.getCreateAt())
                .visibility(member.isVisibility())
                .follower(member.getFollower().size())
                .following(member.getFollowing().size())
                .introduce(member.getIntroduce())
                .profile(member.getProfile()).build();
    }

    public void followMember(FollowDto followDto) {
        Member me = memberFacade.getMemberWithFollower(followDto.getEmail());
        Member follow = memberFacade.getMemberWithFollowing(followDto.getOtherEmail());
        me.getFollowing().remove(follow);
        follow.getFollower().remove(me);
        memberRepository.save(follow);
    }

    public void unFollowMember(FollowDto followDto) {
        Member me = memberFacade.getMemberWithFollower(followDto.getEmail());
        Member follow = memberFacade.getMemberWithFollowing(followDto.getOtherEmail());
        me.getFollowing().remove(follow);
        follow.getFollower().remove(me);
        memberRepository.save(follow);
    }

    public List<MemberView> followers(String email) {
        return memberRepository.searchFollower(email);
    }

    public List<MemberView> following(String email) {
        return memberRepository.searchFollowing(email);
    }

    public void deleteMember(String email) {
        Member member = memberFacade.getMemberWithAll(email);

        member.getFollowing().forEach(m -> {m.getFollower().remove(member); memberRepository.save(m);});

        if (member.getProfile() != null) {
            imageService.deletePath(member.getProfile());
        }

        member.getMakePost().forEach(post -> postService.deletePost(post.getId()));

        commentService.deleteWriter(member);

        memberRepository.delete(member);
    }

    public MemberInfoView infoMember() {
        Member member = memberFacade.getInfoMember();
        return MemberInfoView.builder()
                .visibility(member.isVisibility())
                .introduce(member.getIntroduce())
                .email(member.getEmail())
                .name(member.getName())
                .profile(member.getProfile())
                .build();
    }

    public void setDevice(MemberDeviceTokenDto tokenDto) {
        Member member = memberFacade.getMemberByEmail(tokenDto.getEmail());
        member.setDevice_token(tokenDto.getDevice_token());
        memberRepository.save(member);
    }
}
