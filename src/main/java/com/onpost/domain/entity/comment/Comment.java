package com.onpost.domain.entity.comment;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member writer;

    private String context;

    @Builder
    public Comment(Member writer, String context) {
        this.context = context;
        this.writer = writer;
    }
}
