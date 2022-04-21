package com.onpost.domain.dto.post;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostRequest extends PostDto {

    private final List<MultipartFile> images;
    private final MultipartFile profile;

    public PostRequest(String content, Long id, String title, String introduce, MultipartFile profile, List<MultipartFile> images) {
        super(id ,content, title, introduce);
        this.images = images;
        this.profile = profile;
    }
}
