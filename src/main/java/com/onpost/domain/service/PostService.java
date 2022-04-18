package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.CommentQueryRepository;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import com.onpost.global.error.exception.PageSortException;
import com.querydsl.core.types.OrderSpecifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.onpost.domain.entity.QPost.post;

@Slf4j
@Service
public record PostService(PostQueryRepository postQueryRepository, ImageService imageService,
                          MemberQueryRepository memberQueryRepository, CommentQueryRepository commentQueryRepository) {

    public void createPost(PostRequest postRequest) {
        Set<Image> images = imageService.getImageList(postRequest.getImages(), "static");

        Member writer = memberQueryRepository.findOneWithPost(postRequest.getId());

        Post post = Post.builder()
                .images(images)
                .context(postRequest.getContext())
                .title(postRequest.getTitle())
                .writer(writer)
                .build();

        writer.updatePost(post);

        postQueryRepository.save(post);
        memberQueryRepository.save(writer);
    }

    public PostView showPost(Long id) {
        Post find = postQueryRepository.findPostAll(id);
        return new PostView(find);
    }

    public void like(Long id, Long target) {
        Post find = postQueryRepository.findPostWithLike(target);
        Member member = memberQueryRepository.findMember(id);
        find.getPostLike().add(member);
        postQueryRepository.save(find);
    }

    public void unlike(Long id, Long target) {
        Post find = postQueryRepository.findPostWithLike(target);
        Member member = memberQueryRepository.findMember(id);
        find.getPostLike().remove(member);
        postQueryRepository.save(find);
        memberQueryRepository.save(member);
    }

    public PostView editPost(PostRequest per) {
        Post find = postQueryRepository.findPostAll(per.getId());

        if (per.getContext() != null) {
            find.setContext(per.getContext());
        }

        if (per.getTitle() != null) {
            find.setTitle(per.getTitle());
        }

        Set<Image> dummy = Set.copyOf(find.getImages());
        find.getImages().clear();

        if (per.getImages() != null) {
            find.setImages(imageService.getImageList(per.getImages(), "static"));
        }

        postQueryRepository.save(find);

        imageService.deleteImageList(dummy);

        return new PostView(find);
    }

    public List<PostResponse> pagePost(String sort, Long page) {
        OrderSpecifier<?> orderBy = switch (sort) {
            case "new" -> post.id.desc();
            case "like" -> post.postLike.size().desc();
            default -> throw PageSortException.EXCEPTION;
        };
        return postQueryRepository.findPage(orderBy, page);
    }

    public void deletePost(Long id) {
        Post find = postQueryRepository.findPostAll(id);

        Member member = memberQueryRepository.findOneWithPost(find.getWriter().getId());
        member.deletePost(find);
        memberQueryRepository.save(member);

        Set<Image> images = Set.copyOf(find.getImages());
        find.getImages().clear();

        Set<MainComment> comments = Set.copyOf(find.getComments());
        find.getComments().clear();
        comments.forEach(commentQueryRepository::delete);

        find.getPostLike().clear();

        postQueryRepository.delete(find);

        imageService.deleteImageList(images);
    }
}
