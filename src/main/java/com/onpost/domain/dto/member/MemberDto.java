package com.onpost.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class MemberDto {

    private Long id;
    private String name;
    private String introduce;
}
