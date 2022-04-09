package com.onpost.domain.repository;

import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.jpa.PostRepository;
import com.onpost.domain.service.ImageService;
import com.onpost.global.error.exception.PostNotFoundException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.onpost.domain.entity.QPost.post;

@Slf4j
@Repository
public class PostQueryRepository extends QuerydslRepositorySupport {

    private final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ImageService imageService;

    public PostQueryRepository(PostRepository postRepository,
                               JPAQueryFactory jpaQueryFactory,
                               ImageService imageService) {
        super(Post.class);
        this.postRepository = postRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.imageService = imageService;
    }

    public Post show(Long id) {
        Post find = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.writer)
                .fetchJoin()
                .leftJoin(post.images)
                .fetchJoin()
                .leftJoin(post.postLike)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();

        return check(find);
    }

    public List<PostResponse> showPage(OrderSpecifier<?> sort, Long page) {
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

    public void save(Post post) {
        postRepository.save(post);
    }

    public void delete(Post post) {
        post.getWriter().deletePost(post);
        imageService.deleteImageList(post.getImages());
        postRepository.delete(post);
    }

    private Post check(Post post) {
        if(post == null) {
            throw PostNotFoundException.EXCEPTION;
        }
        return post;
    }
}
