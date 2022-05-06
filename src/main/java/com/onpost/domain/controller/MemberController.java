package com.onpost.domain.controller;

import com.onpost.domain.dto.IDValueDto;
import com.onpost.domain.dto.member.*;
import com.onpost.domain.service.MemberService;
import com.onpost.global.error.validation.EditGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/edit")
    public void edit(@Validated({EditGroup.class}) @ModelAttribute MemberRequest memberDto) {
        memberService.editMember(memberDto);
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

    @GetMapping("/followers")
    public List<MemberView> follower(@RequestParam Long id) {
        return memberService.followers(id);
    }

    @GetMapping("/following")
    public List<MemberView> following(@RequestParam Long id) {
        return memberService.following(id);
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
