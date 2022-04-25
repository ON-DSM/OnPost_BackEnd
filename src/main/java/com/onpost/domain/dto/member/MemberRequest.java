package com.onpost.domain.dto.member;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MemberRequest extends MemberDto {

    private final MultipartFile profile;

    public MemberRequest(Long id, String name,
                         String introduce, MultipartFile profile) {
        super(id, name, introduce);
        this.profile = profile;
    }
}
