package com.onpost.domain.dto.post;

import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostView extends PostDto {

    private final String profile;
    private final MemberView writer;
    private final List<Long> like;
    private final List<String> images;
    private final List<String> tags;
    private final LocalDateTime createAt;
    private final List<MainCommentResponse> comments;

    public PostView(Post post, List<MainComment> comments) {
        super(post.getId(), post.getContent(), post.getTitle(), post.getIntroduce());
        this.images = post.getImages().stream().map(Image::getImagePath).toList();
        this.createAt = post.getCreateAt();
        this.writer = new MemberView(post.getWriter());
        this.like = post.getPostLike().stream().map(Member::getId).toList();
        this.tags = List.of(post.getTags().split(","));
        this.comments = comments.stream().map(MainCommentResponse::new).toList();
        this.profile = post.getProfileImage();
    }
}
