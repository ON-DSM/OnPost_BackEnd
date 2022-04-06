package com.onpost.domain.dto.post;

import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse extends PostDto {

    private final MemberView writer;
    private final Integer like;
    private final List<Image> images;
    private final LocalDateTime createAt;

    @QueryProjection
    public PostResponse(Long id, String context, String title, Member writer, Integer like, List<Image> images, LocalDateTime createAt) {
        super(id, context, title);
        this.like = like;
        this.writer = new MemberView(writer);
        this.images = images;
        this.createAt = createAt;
    }

    public PostResponse(Post post) {
        super(post.getId(), post.getContext(), post.getTitle());
        this.images = post.getImages();
        this.like = post.getPostLike().size();
        this.writer = new MemberView(post.getWriter());
        this.createAt = post.getCreateAt();
    }
}
