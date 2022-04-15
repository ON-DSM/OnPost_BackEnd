package com.onpost.domain.controller;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.member.*;
import com.onpost.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/edit")
    public MemberView edit(@Valid @ModelAttribute MemberRequest memberDto) {
        return memberService.editMember(memberDto);
    }

    @GetMapping("/profile")
    public MemberResponse show(@RequestParam Long id) {
        return memberService.showMember(id);
    }

    @PutMapping("/follow")
    public void follow(@Valid @RequestBody IDValueDto IDValueDto) {
        memberService.followMember(IDValueDto, true);
    }

    @DeleteMapping("/unfollow")
    public void unfollow(@Valid @RequestBody IDValueDto IDValueDto) {
        memberService.followMember(IDValueDto, false);
    }

    @GetMapping("/link")
    public FollowResponse linkMember(@RequestParam Long id) {
        return memberService.followList(id);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        memberService.deleteMember(id);
    }

    @GetMapping("/info")
    public MemberView info() {
        return memberService.infoMember();
    }
}
