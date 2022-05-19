package com.onpost.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostCreateRequest {

    private String email;
    private String introduce;
    private String title;
    private String content;
    private String tags;
    private MultipartFile profile;
    private List<MultipartFile> images;
}
