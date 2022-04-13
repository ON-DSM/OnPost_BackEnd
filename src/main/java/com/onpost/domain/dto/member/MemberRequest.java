package com.onpost.domain.dto.member;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class MemberRequest extends MemberDto {

    private final MultipartFile profile;

    public MemberRequest(Long id, @NotBlank(message = "이름은 6자 이상, 16자 이하이여야 합니다.") @Size(min = 6, max = 16) String name,
                         String introduce, MultipartFile profile) {
        super(id, name, introduce);
        this.profile = profile;
    }
}
