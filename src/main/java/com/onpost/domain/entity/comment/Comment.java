package com.onpost.domain.entity.comment;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.member.Member;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn
@Getter
@EqualsAndHashCode
public abstract class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id")
    private Member writer;

    private String content;

    public void setContext(String content) {
        this.content = content;
    }

    public Comment(Member writer, String content) {
        this.content = content;
        this.writer = writer;
    }
}
