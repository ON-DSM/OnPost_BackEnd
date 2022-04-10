package com.onpost.domain.dto.post;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostRequest extends PostDto {

    private final List<MultipartFile> images;

    public PostRequest(@NotBlank(message = "내용이 들어가야합니다!") String context,
                       @NotNull(message = "작성자 Id가 없습니다!") Long id,
                       @NotBlank(message = "제목이 들어가야합니다!") String title,
                       List<MultipartFile> images) {
        super(id ,context, title);
        this.images = images;
    }
}
