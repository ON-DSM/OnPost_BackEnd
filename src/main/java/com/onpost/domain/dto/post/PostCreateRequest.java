package com.onpost.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@Getter
public class PostCreateRequest {

    @Email(message = "이메일 유형이여야 합니다.")
    private String email;

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;
    private String tags;
    private String introduce;
    private MultipartFile profile;
    private List<MultipartFile> images;
}
