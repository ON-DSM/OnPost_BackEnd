package com.onpost.domain.entity.comment;

import com.onpost.domain.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "Sub")
public class SubComment extends Comment {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "main_comment_id")
    private MainComment main;

    @Builder
    public SubComment(String content, Member writer, MainComment main) {
        super(writer, content);
        this.main = main;
    }
}
