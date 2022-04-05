package com.onpost.domain.controller;

import com.onpost.domain.dto.member.MemberRequest;
import com.onpost.domain.dto.member.MemberResponse;
import com.onpost.domain.entity.Member;
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
    public Member editMember(@Valid @RequestBody MemberRequest memberDto) {
        return memberService.edit(memberDto);
    }

    @GetMapping("/Profile")
    public MemberResponse getProfile(@RequestParam Long id) {
        return memberService.showMember(id);
    }
}
