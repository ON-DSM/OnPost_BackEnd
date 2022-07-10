package com.onpost.domain.service;

import com.onpost.domain.dto.FollowDto;
import com.onpost.domain.dto.member.MemberEditRequest;
import com.onpost.domain.dto.member.MemberPasswordRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.repository.MemberRepository;
import com.onpost.global.error.exception.PasswordNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.onpost.domain.service.AuthService.DEFAULT_IMAGE;

@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class MemberService {

    private final MemberFacade memberFacade;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PostService postService;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder;

    public MemberView editMember(MemberEditRequest request) {
        Member member = memberFacade.getMemberByEmail(request.getEmail());
        member.setName(request.getName());
        member.setIntroduce(request.getIntroduce());

        if (request.getProfile() != null) {
            if (!member.getProfile().equals(DEFAULT_IMAGE)) {
                imageService.deletePath(member.getProfile());
            }
            member.setProfile(imageService.getPath(request.getProfile(), "profile"));
        }

        member = memberRepository.save(member);

        return MemberView.builder()
                .profile(member.getProfile())
                .name(member.getName())
                .email(member.getEmail())
                .introduce(member.getIntroduce())
                .profile(member.getProfile())
                .build();
    }

    public MemberResponse showMember(String email) {
        Member member = memberFacade.getMemberProfile(email);

        return MemberResponse.builder()
                .name(member.getName())
                .email(member.getEmail())
                .createAt(member.getCreateAt())
                .follower(member.getFollower().size())
                .following(member.getFollowing().size())
                .introduce(member.getIntroduce())
                .profile(member.getProfile()).build();
    }

    public void followMember(FollowDto followDto) {
        Member me = memberFacade.getMemberWithFollower(followDto.getEmail());
        Member follow = memberFacade.getMemberWithFollowing(followDto.getOtherEmail());
        me.getFollowing().add(follow);
        follow.getFollower().add(me);
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

        if (member.getProfile() != null && !member.getProfile().equals(DEFAULT_IMAGE)) {
            imageService.deletePath(member.getProfile());
        }

        member.getMakePost().forEach(post -> postService.deletePost(post.getId()));

        commentService.deleteWriter(member);

        memberRepository.delete(member);
    }

    public MemberView infoMember() {
        Member member = memberFacade.getInfoMember();
        return MemberView.builder()
                .introduce(member.getIntroduce())
                .email(member.getEmail())
                .name(member.getName())
                .profile(member.getProfile())
                .build();
    }

    public void changePw(MemberPasswordRequest request) {
        Member member = memberFacade.getMemberByEmail(request.getEmail());

        if(!passwordEncoder.matches(request.getOriginPassword(), member.getPassword())) {
            throw PasswordNotMatchException.EXCEPTION;
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }
}
