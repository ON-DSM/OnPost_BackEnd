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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.onpost.domain.entity.QPost.post;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class PostService {

    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;
    private final MemberQueryRepository memberQueryRepository;
    private final CommentQueryRepository commentQueryRepository;


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

    public void like(Long id, Long people) {
        Post find = postQueryRepository.findPostWithLike(id);
        Member member = memberQueryRepository.findOneWithLike(people);
        find.getPostLike().add(member);
        member.getPostLike().add(find);
        postQueryRepository.save(find);
        memberQueryRepository.save(member);
    }

    public void unlike(Long id, Long people) {
        Post find = postQueryRepository.findPostWithLike(id);
        Member member = memberQueryRepository.findOneWithLike(people);
        find.getPostLike().remove(member);
        member.getPostLike().remove(find);
        postQueryRepository.save(find);
        memberQueryRepository.save(member);
    }

    public PostView editPost(PostRequest per) {
        Post find = postQueryRepository.findPostAll(per.getId());

        if(per.getContext() != null) {
            find.setContext(per.getContext());
        }

        if(per.getTitle() != null) {
            find.setTitle(per.getTitle());
        }

        imageService.deleteImageList(find.getImages());

        if(per.getImages() != null) {
            find.setImages(imageService.getImageList(per.getImages(), "static"));
        }

        postQueryRepository.save(find);

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
        Post post = postQueryRepository.findPostAll(id);

        Member member = memberQueryRepository.findOneWithPost(post.getWriter().getId());
        member.deletePost(post);
        memberQueryRepository.save(member);

        Set<Image> dummy = Set.copyOf(post.getImages());
        post.getImages().clear();
        imageService.deleteImageList(dummy);

        Set<MainComment> comments = Set.copyOf(post.getComments());
        post.getComments().clear();
        comments.forEach(commentQueryRepository::delete);

        post.getPostLike().clear();

        postQueryRepository.delete(post);
    }
}
