package com.onpost.domain.dto.post;

import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse extends PostDto {

    private final MemberView writer;
    private final Integer like;
    private final List<Image> images;
    private final LocalDateTime createAt;

    public PostResponse(Post post) {
        super(post.getId(), post.getContext(), post.getTitle());
        this.images = post.getImages();
        this.like = post.getPostLike().size();
        this.writer = new MemberView(post.getWriter());
        this.createAt = post.getCreateAt();
    }
}
