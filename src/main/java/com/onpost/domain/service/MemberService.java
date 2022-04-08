package com.onpost.domain.service;

import com.onpost.domain.dto.member.FollowDto;
import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import com.onpost.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final S3Uploader s3Uploader;
    private final PostQueryRepository postQueryRepository;

    public Member editMember(MemberRequest memberRequest) {
        Member member = memberQueryRepository.getCurrentMember(memberRequest.getId());

        if(memberRequest.getName() != null) {
            member.setName(memberRequest.getName());
        }

        if (memberRequest.getIntroduce() != null) {
            member.setIntroduce(memberRequest.getIntroduce());
        }

        if(memberRequest.getProfile() != null) {
            if(member.getProfile() != null) {
                s3Uploader.delete(member.getProfile());
            }
            member.setProfile(s3Uploader.upload(memberRequest.getProfile(), "profile"));
        }

        return memberQueryRepository.save(member);
    }

    public MemberResponse showMember(Long id) {
        return memberQueryRepository.getView(id);
    }

    public void followMember(FollowDto followDto) {
        Member me = memberQueryRepository.getFollowRelation(followDto.getId());
        Member follow = memberQueryRepository.getFollowRelation(followDto.getFollowId());

        me.followMe(follow);

        memberQueryRepository.save(me);
        memberQueryRepository.save(follow);
    }

    public void deleteMember(Long id) {
        Member member = memberQueryRepository.deleteDummy(id);
        for(Member m : member.getFollowing()) {
            m.unfollowMe(member);
        }

        for(Member m : member.getFollower()) {
            member.unfollowMe(m);
        }

        s3Uploader.delete(member.getProfile());

        for(Post p : member.getMakePost()) {
            postQueryRepository.delete(p);
        }

        memberQueryRepository.delete(member);
    }
}
