package com.onpost.domain.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoView extends MemberView {

    private boolean visibility;

    @Builder
    public MemberInfoView(String profile, String email, String name, String introduce, boolean visibility) {
        super(email, name, introduce, profile);
        this.visibility = visibility;
    }
}
