package com.onpost.domain.dto.post;

import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostView extends PostDto {

    private final MemberView writer;
    private final Integer like;
    private final List<Image> images;
    private final LocalDateTime createAt;
    private final List<MainCommentResponse> comments;

    public PostView(Post post) {
        super(post.getId(), post.getContext(), post.getTitle());
        this.writer = new MemberView(post.getWriter());
        this.createAt = post.getCreateAt();
        this.like = post.getPostLike().size();
        this.images = post.getImages().stream().toList();
        this.comments = post.getComments().stream().map(MainCommentResponse::new).collect(Collectors.toList());
    }
}
