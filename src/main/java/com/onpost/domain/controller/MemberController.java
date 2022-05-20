package com.onpost.domain.controller;

import com.onpost.domain.dto.FollowDto;
import com.onpost.domain.dto.member.*;
import com.onpost.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/edit")
    public void edit(@Valid @ModelAttribute MemberEditRequest memberDto) {
        memberService.editMember(memberDto);
    }

    @GetMapping("/profile")
    public MemberResponse show(@RequestParam String email) {
        return memberService.showMember(email);
    }

    @PutMapping("/follow")
    public void follow(@Valid @RequestBody FollowDto idDto) {
        memberService.followMember(idDto);
    }

    @DeleteMapping("/unfollow")
    public void unfollow(@Valid @RequestBody FollowDto idDto) {
        memberService.unFollowMember(idDto);
    }

    @GetMapping("/followers")
    public List<MemberView> follower(@RequestParam String email) {
        return memberService.followers(email);
    }

    @GetMapping("/following")
    public List<MemberView> following(@RequestParam String email) {
        return memberService.following(email);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String email) {
        memberService.deleteMember(email);
    }

    @GetMapping("/info")
    public MemberInfoView info() {
        return memberService.infoMember();
    }

    @PostMapping("/device")
    public void device(@RequestBody @Valid MemberDeviceTokenRequest tokenDto) {
        memberService.setDevice(tokenDto);
    }
    
    @PostMapping("/password")
    public void changePassword(@RequestBody @Valid MemberPasswordRequest request) {
        memberService.changePw(request);
    }

    @PutMapping("/visibility")
    public void changeVisibility(@RequestBody @Valid MemberVisibilityRequest request) {
        memberService.setVisibility(request);
    }
}
