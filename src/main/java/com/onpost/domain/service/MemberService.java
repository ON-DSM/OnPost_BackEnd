package com.onpost.domain.service;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.member.*;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final ImageService imageService;
    private final PostService postService;

    public MemberView editMember(MemberRequest memberRequest) {
        Member member = memberQueryRepository.findMember(memberRequest.getId());

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
        return new MemberResponse(memberQueryRepository.findOneWithAll(id));
    }

    public void followMember(IDValueDto IDValueDto, boolean positive) {
        Member me = memberQueryRepository.findOneWithFollow(IDValueDto.getId());
        Member follow = memberQueryRepository.findOneWithFollow(IDValueDto.getFollowId());

        if(positive) {
            follow.followMe(me);
        } else {
            follow.unfollowMe(me);
        }

        memberQueryRepository.save(me);
        memberQueryRepository.save(follow);
    }

    public FollowResponse followList(Long id) {
        Member member = memberQueryRepository.findOneWithFollow(id);
        return FollowResponse.builder()
                .follower(member.getFollower().stream().map(MemberView::new).collect(Collectors.toList()))
                .following(member.getFollowing().stream().map(MemberView::new).collect(Collectors.toList()))
                .build();
    }

    public void deleteMember(Long id) {
        Member member = memberQueryRepository.findOneWithAll(id);

        member.getFollower().forEach(m -> m.followMe(member));
        member.getFollower().forEach(member::unfollowMe);

        List<Post> posts = List.copyOf(member.getMakePost());
        posts.forEach(p -> postService.deletePost(p.getId()));

        if(member.getProfile() != null) {
            imageService.deletePath(member.getProfile());
        }

        memberQueryRepository.delete(member);
    }

    public MemberView infoMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberQueryRepository.findOneByEmail(email);
        return new MemberView(member);
    }
}
