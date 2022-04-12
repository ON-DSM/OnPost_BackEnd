package com.onpost.domain.entity.comment;

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

    @OneToMany
    @JoinTable(
            name = "child",
            joinColumns = {@JoinColumn(name = "parent_id")},
            inverseJoinColumns = {@JoinColumn(name = "child_id")}
    )
    private final Set<SubComment> subComments = new LinkedHashSet<>();

    @Builder
    public MainComment(String context, Member writer) {
        super(writer, context);
    }
}
