package com.onpost.domain.entity;

import com.onpost.domain.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member writer;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String context;

    @OneToMany
    @JoinTable(
            name = "images",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id")}
    )
    private List<Image> images;

    @OneToMany
    @JoinTable(
            name = "postLike",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<Member> postLike;

    public void setContext(String context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
