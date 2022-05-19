package com.onpost.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotNull(message = "아이디를 찾을 수 없습니다.")
    private Long id;

    private String content;
    private String title;
    private String introduce;
    private MultipartFile profile;
    private List<MultipartFile> images;

    @Size(max = 200, message = "태그가 너무 길거나 많습니다.")
    private String tags;
}
