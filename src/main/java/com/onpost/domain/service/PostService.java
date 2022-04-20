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
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class PostService {

    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;
    private final MemberQueryRepository memberQueryRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final CommentService commentService;

    public void createPost(PostRequest postRequest) {

        Member writer = memberQueryRepository.findOneWithPost(postRequest.getId());

        Set<Image> images = imageService.getImageList(postRequest.getImages(), "static");

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
        Post find = postQueryRepository.findOneWithAll(id);
        List<MainComment> comments = commentQueryRepository.findMainByParent(id);
        return new PostView(find, comments);
    }

    public void like(Long id, Long target) {
        Post find = postQueryRepository.findOneWithLike(target);
        Member member = memberQueryRepository.findMember(id);
        find.getPostLike().add(member);
        postQueryRepository.save(find);
    }

    public void unlike(Long id, Long target) {
        Post find = postQueryRepository.findOneWithLike(target);
        Member member = memberQueryRepository.findMember(id);
        find.getPostLike().remove(member);
        postQueryRepository.save(find);
        memberQueryRepository.save(member);
    }

    public void editPost(PostRequest per) {
        Post find = postQueryRepository.findOneEdit(per.getId());

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
        Post find = postQueryRepository.findOneWithAll(id);
        List<MainComment> comments = commentQueryRepository.findMainByParent(id);
        Member member = memberQueryRepository.findOneWithPost(find.getWriter().getId());
        member.getMakePost().remove(find);
        memberQueryRepository.save(member);

        Set<Image> images = Set.copyOf(find.getImages());
        find.getImages().clear();
        find.getComments().clear();
        find.getPostLike().clear();

        postQueryRepository.delete(find);

        comments.forEach(commentService::deleteMain);
        imageService.deleteImageList(images);
    }
}
