package com.onpost.domain.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostTop3Response extends PostResponse {

    private int rank;

    public PostTop3Response(PostResponse postResponse, int rank) {
        super(postResponse.getId(), postResponse.getTitle(), postResponse.getIntroduce(),
                postResponse.getProfileImage(), postResponse.getComments(), postResponse.getLike(),
                postResponse.getWriter(), postResponse.getCreateAt(), postResponse.getTags());
        this.rank = rank;
    }
}
