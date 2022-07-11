package com.onpost.domain.entity;

import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private Member writer;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String profileImage;

    private String introduce;

    private String tags;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "post_like",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private final Set<Member> postLike = new LinkedHashSet<>();

    @OneToMany(mappedBy = "parent_post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<MainComment> comments = new LinkedHashSet<>();
}
