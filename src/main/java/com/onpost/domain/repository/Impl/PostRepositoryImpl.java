package com.onpost.domain.repository.impl;

import com.onpost.domain.dto.member.QMemberView;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.QPostResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.repository.custom.CustomPostRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.onpost.domain.entity.QPost.post;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<PostResponse> searchMainPage(Sort sort, Long page) {
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.title, post.introduce, post.profileImage,
                        post.comments.size().longValue(),
                        post.postLike.size().longValue(),
                        new QMemberView(post.writer.email, post.writer.name, post.writer.introduce, post.writer.profile),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .orderBy(setSort(sort))
                .limit(16L)
                .offset((page - 1L) * 16L)
                .fetch();
    }

    @Override
    public List<PostResponse> searchMemberPosts(String email) {
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.title, post.introduce, post.profileImage,
                        post.comments.size().longValue(),
                        post.postLike.size().longValue(),
                        new QMemberView(post.writer.email, post.writer.name, post.writer.introduce, post.writer.profile),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .where(post.writer.email.eq(email))
                .orderBy(post.createAt.desc()).fetch();
    }

    @Override
    public List<PostResponse> searchPosts(String param) {
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.title, post.introduce, post.profileImage,
                        post.comments.size().longValue(),
                        post.postLike.size().longValue(),
                        new QMemberView(post.writer.email, post.writer.name, post.writer.introduce, post.writer.profile),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .where(post.title.contains(param)
                        .or(post.tags.contains(param))
                        .or(post.introduce.contains(param)))
                .orderBy(post.createAt.desc()).fetch();
    }

    @Override
    public List<PostResponse> searchTop3(Sort sort) {
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.title, post.introduce, post.profileImage,
                        post.comments.size().longValue(),
                        post.postLike.size().longValue(),
                        new QMemberView(post.writer.email, post.writer.name, post.writer.introduce, post.writer.profile),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .orderBy(setSort(sort))
                .limit(3L).fetch();
    }

    private OrderSpecifier<?> setSort(Sort sort) {
        return switch (sort) {
            case NEW -> post.id.desc();
            case LIKE -> post.postLike.size().desc();
            case COMMENTS -> post.comments.size().desc();
        };
    }
}
