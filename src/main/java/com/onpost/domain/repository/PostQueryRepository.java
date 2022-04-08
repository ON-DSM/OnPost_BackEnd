package com.onpost.domain.repository;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.jpa.MemberRepository;
import com.onpost.domain.repository.jpa.PostRepository;
import com.onpost.domain.service.ImageService;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.onpost.domain.entity.QPost.post;
import static com.onpost.domain.entity.member.QMember.member;

@Repository
public class PostQueryRepository extends QuerydslRepositorySupport {

    private final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ImageService imageService;
    private final MemberRepository memberRepository;

    public PostQueryRepository(PostRepository postRepository, JPAQueryFactory jpaQueryFactory, ImageService imageService, MemberRepository memberRepository) {
        super(Post.class);
        this.postRepository = postRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.imageService = imageService;
        this.memberRepository = memberRepository;
    }

    public PostResponse create(PostRequest postRequest) {
        List<Image> images = imageService.getImageList(postRequest.getImages(), "static");

        Member writer = jpaQueryFactory.selectFrom(member)
                .leftJoin(member.makePost)
                .fetchJoin()
                .where(member.id.eq(postRequest.getId()))
                .fetchOne();

        Post post = Post.builder()
                .postLike(new LinkedHashSet<>())
                .images(images)
                .context(postRequest.getContext())
                .title(postRequest.getTitle())
                .writer(writer)
                .build();

        writer.updatePost(post);

        postRepository.save(post);
        memberRepository.save(writer);

        return new PostResponse(post);
    }

    public PostResponse show(Long id) {
         Post find = jpaQueryFactory.selectFrom(post)
                 .leftJoin(post.writer)
                 .fetchJoin()
                 .leftJoin(post.images)
                 .fetchJoin()
                 .leftJoin(post.postLike)
                 .fetchJoin()
                 .where(post.id.eq(id))
                 .fetchOne();
         return new PostResponse(find);
    }

    public List<PostResponse> showPost(OrderSpecifier<?> sort, Long page) {
        List<Post> posts = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.writer)
                .fetchJoin()
                .leftJoin(post.images)
                .fetchJoin()
                .leftJoin(post.postLike)
                .fetchJoin()
                .orderBy(sort)
                .limit(16L)
                .offset((page - 1L) * 16L)
                .fetch();
        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public PostResponse editPost(PostRequest per) {
        Post query = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.images)
                .fetchJoin()
                .where(post.id.eq(per.getId()))
                .fetchOne();

        if(per.getContext() != null) {
            query.setContext(per.getContext());
        }

        if(per.getTitle() != null) {
            query.setTitle(per.getTitle());
        }

        if(per.getImages() != null) {
            imageService.deleteImageList(query.getImages());
            query.setImages(imageService.getImageList(per.getImages(), "static"));
        }

        postRepository.save(query);

        return new PostResponse(query);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }
}
