package com.onpost.domain.entity.comment;

import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Main")
@Getter
public class MainComment extends Comment {

    @OneToMany(mappedBy = "main", cascade = CascadeType.ALL)
    private final Set<SubComment> subComments = new LinkedHashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_post_post_id")
    private Post parent_post;

    @Builder
    public MainComment(String content, Member writer, Post post) {
        super(writer, content);
        this.parent_post = post;
    }
}
