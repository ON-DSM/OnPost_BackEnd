package com.onpost.domain.repository;

import com.onpost.domain.dto.post.PostDto;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.PostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.onpost.domain.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Post create(PostDto postDto) {
        Member writer = jpaQueryFactory.selectFrom(member)
                .where(member.id.eq(postDto.getWriterId()))
                .fetchOne();

        Post post = Post.builder()
                .images(postDto.getImages())
                .context(postDto.getContext())
                .title(postDto.getTitle())
                .writer(writer)
                .build();

        return postRepository.save(post);
    }
}
