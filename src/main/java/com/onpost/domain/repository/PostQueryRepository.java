package com.onpost.domain.repository;

import com.onpost.domain.dto.member.QMemberView;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.jpa.PostRepository;
import com.onpost.global.error.exception.PostNotFoundException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.onpost.domain.entity.QPost.post;
import static com.onpost.domain.entity.comment.QMainComment.mainComment;
import static com.onpost.domain.entity.member.QMember.member;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.select;

@Slf4j
@Repository
public class PostQueryRepository extends QuerydslRepositorySupport {

    private final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public PostQueryRepository(PostRepository postRepository,
                               JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.postRepository = postRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Post findOneWithImages(Long id) {
        Post find = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.images)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public Post findOneWithAll(Long id) {
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

    public Post findOneWithWriterAndImagesAndPostLike(Long id) {
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

    public List<PostResponse> findPage(OrderSpecifier<?> sort, Long page) {
        return jpaQueryFactory.select(Projections.constructor(
                        PostResponse.class,
                        post.id, post.content, post.title, post.introduce, post.profileImage,
                        select(count(mainComment)).from(mainComment).where(mainComment.parent_post.eq(post)),
                        select(count(member)).from(member).where(post.postLike.contains(member)),
                        new QMemberView(post.writer),
                        post.createAt
                )).from(post)
                .orderBy(sort)
                .limit(16L)
                .offset((page - 1L) * 16L)
                .fetch();
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void delete(Post find) {
        postRepository.delete(find);
    }

    public Post findOneWithComment(Long id) {
        Post find = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.comments)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();
        return check(find);
    }

    public Post findOneWithLike(Long id) {
        Post find = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.postLike)
                .fetchJoin()
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
}
