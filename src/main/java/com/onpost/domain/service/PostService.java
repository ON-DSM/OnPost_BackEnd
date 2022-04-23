package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
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

        Post post = Post.builder()
                .introduce(postRequest.getIntroduce())
                .content(postRequest.getContent())
                .title(postRequest.getTitle())
                .tags(postRequest.getTags())
                .writer(writer)
                .build();

        if(postRequest.getProfile() != null) {
            post.setProfileImage(imageService.getPath(postRequest.getProfile(), "profile"));
        }
        imageService.addImageList(postRequest.getImages(), "static", post);

        writer.updatePost(post);

        postQueryRepository.save(post);
    }

    public PostView showPost(Long id) {
        Post find = postQueryRepository.findOneWithWriterAndImagesAndPostLike(id);
        List<MainComment> comments = commentQueryRepository.findMainByParent(find);
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
        find.getPostLike().removeIf(member -> member.getId().equals(id));
        postQueryRepository.save(find);
    }

    public void editPost(PostRequest per) {
        Post find = postQueryRepository.findOneWithImages(per.getId());

        if(per.getIntroduce() != null) {
            find.setIntroduce(per.getIntroduce());
        }

        if (per.getContent() != null) {
            find.setContent(per.getContent());
        }

        if (per.getTitle() != null) {
            find.setTitle(per.getTitle());
        }

        if(per.getProfile() != null) {
            if(find.getProfileImage() != null) {
                imageService.deletePath(find.getProfileImage());
            }
            find.setProfileImage(imageService.getPath(per.getProfile(), "profile"));
        }

        imageService.deleteImageList(find.getImages());

        find.setTags(per.getTags());

        if (per.getImages() != null) {
            imageService.addImageList(per.getImages(), "static", find);
        }

        postQueryRepository.save(find);
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
        List<MainComment> comments = commentQueryRepository.findMainByParent(find);

        find.getWriter().getMakePost().remove(find);

        find.getPostLike().clear();

        comments.forEach(commentService::deleteMain);

        if(find.getProfileImage() != null) {
            imageService.deletePath(find.getProfileImage());
        }
        imageService.deleteImageList(find.getImages());

        postQueryRepository.delete(find);
    }
}
