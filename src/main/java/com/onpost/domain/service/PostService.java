package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.facade.PostFacade;
import com.onpost.domain.repository.CommentRepository;
import com.onpost.domain.repository.PostRepository;
import com.onpost.global.annotation.ServiceSetting;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ServiceSetting
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostFacade postFacade;
    private final ImageService imageService;
    private final MemberFacade memberFacade;
    private final CommentRepository commentRepository;

    public void createPost(PostRequest postRequest) {

        Member writer = memberFacade.getMemberWithPost(postRequest.getId());

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
        Post find = postFacade.getPostWithWriterAndImagesAndLike(id);
        List<MainComment> comments = commentRepository.findMainByPost(find);
        return new PostView(find, comments);
    }

    public void like(Long id, Long target) {
        Post find = postFacade.getPostWithLike(target);
        Member member = memberFacade.getMember(id);
        find.getPostLike().add(member);
        postRepository.save(find);
    }

    public void unlike(Long id, Long target) {
        Post find = postFacade.getPostWithLike(target);
        find.getPostLike().removeIf(member -> member.getId().equals(id));
        postRepository.save(find);
    }

    public void editPost(PostRequest per) {
        Post find = postFacade.getPostWithImages(per.getId());

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

        find.setTags(per.getTags());

        if (per.getImages() != null) {
            imageService.deleteImageList(find.getImages());
            imageService.addImageList(per.getImages(), "static", find);
        }

        postRepository.save(find);
    }

    public List<PostResponse> pagePost(Sort sort, Long page) {
        return postRepository.searchMainPage(sort, page);
    }

    public void deletePost(Long id) {
        Post find = postFacade.getPostWithAll(id);

        if(find.getProfileImage() != null) {
            imageService.deletePath(find.getProfileImage());
        }
        imageService.deleteImageList(find.getImages());

        postRepository.delete(find);
    }
}
