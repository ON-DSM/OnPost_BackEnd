package com.onpost.domain.entity.member;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.Post;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

    private String certified;

    private boolean visibility;

    private String device_token;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}