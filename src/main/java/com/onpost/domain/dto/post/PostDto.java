package com.onpost.domain.dto.post;

import com.onpost.domain.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class PostDto {

    @NotBlank(message = "내용이 들어가야합니다!")
    private String context;

    @NotNull(message = "작성자 Id가 없습니다!")
    private Long writerId;

    @NotBlank(message = "제목이 들어가야합니다!")
    private String title;

    private List<Image> images;
}
