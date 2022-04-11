package com.onpost.domain.repository;

import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.jpa.PostRepository;
import com.onpost.domain.service.ImageService;
import com.onpost.global.error.exception.PostNotFoundException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.onpost.domain.entity.QPost.post;

@Slf4j
@Repository
@Transactional
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

    public Post findPost(Long id) {
        Post find = findBase()
                .leftJoin(post.images.get(0))
                .fetchJoin()
                .leftJoin(post.postLike)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public Post findPostAll(Long id) {
        Post find = findBase()
                .leftJoin(post.images)
                .fetchJoin()
                .leftJoin(post.postLike)
                .fetchJoin()
                .leftJoin(post.comments)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public List<PostResponse> findPage(OrderSpecifier<?> sort, Long page) {
        List<Post> posts = findBase()
                .leftJoin(post.images.get(0))
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

    public void delete(Post find) {
        deletePostImages(find.getImages());
        postRepository.delete(find);
    }

    public void deletePostImages(List<Image> postImages) {
        List<Image> dummy = List.copyOf(postImages);
        postImages.clear();
        imageService.deleteImageList(dummy);
    }

    public Post findPostWithWriter(Long id) {
        Post find = findBase()
                .where(post.id.eq(id))
                .fetchOne();
        return check(find);
    }

    private Post check(Post post) {
        if(post == null) {
            throw PostNotFoundException.EXCEPTION;
        }
        return post;
    }

    private JPAQuery<Post> findBase() {
        return jpaQueryFactory.selectFrom(post)
                .leftJoin(post.writer)
                .fetchJoin();
    }
}
