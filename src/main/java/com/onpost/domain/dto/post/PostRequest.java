package com.onpost.domain.dto.post;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostRequest extends PostDto {

    private final List<MultipartFile> images;

    public PostRequest(String context, Long id, String title, List<MultipartFile> images) {
        super(id ,context, title);
        this.images = images;
    }
}
