package com.onpost.domain.service;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.member.FollowResponse;
import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PostService postService;
    private final CommentService commentService;

    public void editMember(MemberRequest memberRequest) {
        Member member = memberRepository.findMember(memberRequest.getId());

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
        return new MemberResponse(memberRepository.findOneWithAll(id));
    }

    public void followMember(IDValueDto IDValueDto, boolean positive) {
        Member me = memberRepository.findOneWithFollow(IDValueDto.getId());
        Member follow = memberRepository.findOneWithFollow(IDValueDto.getTargetId());

        if (positive) {
            follow.followMe(me);
        } else {
            follow.unfollowMe(me);
        }

        memberRepository.save(follow);
    }

    public FollowResponse followList(Long id) {
        Member member = memberRepository.findOneWithFollow(id);
        return FollowResponse.builder()
                .follower(member.getFollower().stream().map(MemberView::new).collect(Collectors.toList()))
                .following(member.getFollowing().stream().map(MemberView::new).collect(Collectors.toList()))
                .build();
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findOneWithAll(id);

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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findOneByEmail(email);
        return new MemberView(member);
    }
}
