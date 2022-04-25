package com.onpost.domain.dto.post;

import com.onpost.global.error.validation.EditGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class PostRequest extends PostDto {

    private final List<MultipartFile> images;

    @Size(max = 200, message = "태그가 너무 길거나 많습니다.", groups = {EditGroup.class})
    private final String tags;

    private final MultipartFile profile;

    public PostRequest(String content, Long id, String title, String introduce, MultipartFile profile, List<MultipartFile> images, String tags) {
        super(id ,content, title, introduce);
        this.images = images;
        this.profile = profile;
        this.tags = tags;
    }
}
