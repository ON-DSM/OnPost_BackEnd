package com.onpost.domain.repository.query;

import com.onpost.domain.dto.member.QMemberView;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.QPostResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.repository.Impl.CustomPostRepository;
import com.onpost.global.error.exception.PostNotFoundException;
import com.querydsl.core.types.*;
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
public class PostRepository extends QuerydslRepositorySupport implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostRepository(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
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

    public Post findOneWithWriterAndImagesAndLike(Long id) {
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
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.content, post.title, post.introduce, post.profileImage,
                        select(count(mainComment)).from(mainComment).where(mainComment.parent_post.eq(post)),
                        select(count(member)).from(member).where(post.postLike.contains(member)),
                        new QMemberView(post.writer.id, post.writer.name, post.writer.introduce, post.writer.profile, post.writer.email),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .orderBy(sort)
                .limit(16L)
                .offset((page - 1L) * 16L)
                .fetch();
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
