package com.onpost.domain.repository.impl;

import com.onpost.domain.dto.member.QMemberView;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.QPostResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.repository.custom.CustomPostRepository;
import com.querydsl.core.types.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.onpost.domain.entity.QPost.post;

@Slf4j
public class PostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<PostResponse> searchMainPage(Sort sort, Long page) {
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.content, post.title, post.introduce, post.profileImage,
                        post.comments.size().longValue(),
                        post.postLike.size().longValue(),
                        new QMemberView(post.writer.id, post.writer.name, post.writer.introduce, post.writer.profile, post.writer.email),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .orderBy(setSort(sort))
                .orderBy(post.id.desc())
                .limit(16L)
                .offset((page - 1L) * 16L)
                .fetch();
    }

    @Override
    public List<PostResponse> searchMemberPosts(Long id) {
        return jpaQueryFactory.select(new QPostResponse(
                        post.id, post.content, post.title, post.introduce, post.profileImage,
                        post.comments.size().longValue(),
                        post.postLike.size().longValue(),
                        new QMemberView(post.writer.id, post.writer.name, post.writer.introduce, post.writer.profile, post.writer.email),
                        post.createAt,
                        post.tags
                ))
                .from(post)
                .where(post.writer.id.eq(id))
                .orderBy(post.createAt.desc()).fetch();
    }

    private OrderSpecifier<?> setSort(Sort sort) {
        return switch (sort) {
            case NEW -> post.id.desc();
            case LIKE -> post.postLike.size().desc();
            case COMMENTS -> post.comments.size().desc();
        };
    }
}
