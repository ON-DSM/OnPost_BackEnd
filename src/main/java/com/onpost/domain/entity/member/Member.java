package com.onpost.domain.entity.member;

import com.onpost.domain.entity.BaseEntity;
import com.onpost.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

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
    private Set<Member> follower;

    @OneToMany
    @JoinTable(
            name = "following",
            joinColumns = {@JoinColumn(name = "publisher")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber")}
    )
    private Set<Member> following;

    @OneToMany
    @JoinTable(
            name = "posts",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")}
    )
    private List<Post> makePost;

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public long getFollowingSize() {
        return this.following.size();
    }

    public long getFollowerSize() {
        return this.follower.size();
    }

    public long getMakePost() {
        return this.makePost.size();
    }

    public void followMe(Member follower) {
        this.follower.add(follower);
        follower.following.add(this);
    }

    public void unfollowMe(Member follower) {

    }

    public void updatePost(Post post) {
        this.makePost.add(post);
    }
}