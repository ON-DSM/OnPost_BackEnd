package com.onpost.domain.controller;

import com.onpost.domain.dto.member.FollowDto;
import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.dto.member.MemberView;
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
    public void follow(@Valid @RequestBody FollowDto followDto) {
        memberService.followMember(followDto);
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
