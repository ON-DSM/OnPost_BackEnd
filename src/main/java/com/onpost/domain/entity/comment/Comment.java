package com.onpost.domain.entity.comment;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn
@Getter
public abstract class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(referencedColumnName = "user_id")
    private Member writer;

    private String context;

    public Comment(Member writer, String context) {
        this.context = context;
        this.writer = writer;
    }
}
