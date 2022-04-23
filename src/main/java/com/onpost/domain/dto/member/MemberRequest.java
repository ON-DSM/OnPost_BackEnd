package com.onpost.domain.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class MemberRequest extends MemberDto {

    private MultipartFile profile;

    public MemberRequest(Long id, String name,
                         String introduce, MultipartFile profile) {
        super(id, name, introduce);
        this.profile = profile;
    }
}
