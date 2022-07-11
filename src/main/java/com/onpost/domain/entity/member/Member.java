package com.onpost.domain.entity.member;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.Post;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    @Column(name = "user_name")
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Column(name = "profile_image")
    private String profile;

    private String introduce;

    @Enumerated(value = EnumType.STRING)
    private Authority author;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "follower",
            joinColumns = {@JoinColumn(name = "follower_id")},
            inverseJoinColumns = {@JoinColumn(name = "following_id")}
    )
    private final Set<Member> follower = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "follower", cascade = CascadeType.PERSIST)
    private final Set<Member> following = new LinkedHashSet<>();

    @OneToMany(mappedBy = "writer")
    private final Set<Post> makePost = new LinkedHashSet<>();
}