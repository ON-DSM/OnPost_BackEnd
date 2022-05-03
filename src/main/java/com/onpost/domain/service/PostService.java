package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.query.CommentQueryRepository;
import com.onpost.domain.repository.query.MemberRepository;
import com.onpost.domain.repository.PostRepository;
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

    private final PostRepository postRepository;
    private final ImageService imageService;
    private final MemberRepository memberQueryRepository;
    private final CommentQueryRepository commentQueryRepository;

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

        postRepository.save(post);
    }

    public PostView showPost(Long id) {
        Post find = postRepository.findOneWithWriterAndImagesAndLike(id);
        List<MainComment> comments = commentQueryRepository.findMainByParent(find);
        return new PostView(find, comments);
    }

    public void like(Long id, Long target) {
        Post find = postRepository.findOneWithLike(target);
        Member member = memberQueryRepository.findMember(id);
        find.getPostLike().add(member);
        postRepository.save(find);
    }

    public void unlike(Long id, Long target) {
        Post find = postRepository.findOneWithLike(target);
        find.getPostLike().removeIf(member -> member.getId().equals(id));
        postRepository.save(find);
    }

    public void editPost(PostRequest per) {
        Post find = postRepository.findOneWithImages(per.getId());

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

        postRepository.save(find);
    }

    public List<PostResponse> pagePost(String sort, Long page) {
        OrderSpecifier<?> orderBy = switch (sort) {
            case "new" -> post.id.desc();
            case "like" -> post.postLike.size().desc();
            default -> throw PageSortException.EXCEPTION;
        };
        return postRepository.findPage(orderBy, page);
    }

    public void deletePost(Long id) {
        Post find = postRepository.findOneWithAll(id);

        find.getWriter().getMakePost().remove(find);

        if(find.getProfileImage() != null) {
            imageService.deletePath(find.getProfileImage());
        }
        imageService.deleteImageList(find.getImages());

        postRepository.delete(find);
    }
}
