package com.onpost.domain.entity.member;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
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

    @OneToMany
    @JoinTable(
            name = "follower",
            joinColumns = {@JoinColumn(name = "publisher")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber")}
    )
    private Set<Member> follower = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(
            name = "following",
            joinColumns = {@JoinColumn(name = "publisher")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber")}
    )
    private Set<Member> following = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(
            name = "posts",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")}
    )
    private Set<Post> makePost = new LinkedHashSet<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void followMe(Member other) {
        this.follower.add(other);
        other.following.add(this);
    }

    public void unfollowMe(Member other) {
        this.follower.remove(other);
        other.following.remove(this);
    }

    public void updatePost(Post post) {
        this.makePost.add(post);
    }

    public void deletePost(Post post) {
        this.makePost.remove(post);
    }

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