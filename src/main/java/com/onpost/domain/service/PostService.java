package com.onpost.domain.service;

import com.onpost.domain.dto.LikeDto;
import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.dto.post.PostCreateRequest;
import com.onpost.domain.dto.post.PostEditRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.facade.PostFacade;
import com.onpost.domain.repository.CommentRepository;
import com.onpost.domain.repository.PostRepository;
import com.onpost.global.annotation.ServiceSetting;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@ServiceSetting
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostFacade postFacade;
    private final ImageService imageService;
    private final MemberFacade memberFacade;
    private final CommentRepository commentRepository;

    public void createPost(PostCreateRequest request) {
        Member writer = memberFacade.getMemberWithPost(request.getEmail());

        Post post = Post.builder()
                .introduce(request.getIntroduce())
                .content(request.getContent())
                .title(request.getTitle())
                .tags(request.getTags())
                .writer(writer)
                .build();

        if(request.getProfile() != null) {
            post.setProfileImage(imageService.getPath(request.getProfile(), "profile"));
        }
        imageService.addImageList(request.getImages(), "static", post);

        writer.getMakePost().add(post);
//
//        NotificationMessage message = NotificationMessage.builder().message(new NotificationEvent(writer.getName() + "님이 새로운 게시물을 작성하였습니다.",
//                writer.getFollower().stream().map(Member::getDevice_token).toList()
//        ).getMessage()).build();
//

        postRepository.save(post);
    }

    public PostView showPost(Long id) {
        Post find = postFacade.getPostWithWriterAndImagesAndLike(id);
        List<MainCommentResponse> comments = commentRepository.findMainByPost(find);
        return PostView.builder()
                .id(find.getId())
                .title(find.getTitle())
                .content(find.getContent())
                .createAt(find.getCreateAt())
                .comments(comments)
                .images(find.getImages().stream().map(Image::getImagePath).toList())
                .profile(find.getProfileImage())
                .introduce(find.getIntroduce())
                .tags(Arrays.asList(find.getTags().split(",")))
                .writer(new MemberView(find.getWriter()))
                .like(find.getPostLike().stream().map(Member::getEmail).toList())
                .build();
    }

    public void like(LikeDto likeDto) {
        Post find = postFacade.getPostWithLike(likeDto.getPostId());
        Member member = memberFacade.getMemberByEmail(likeDto.getEmail());
        find.getPostLike().add(member);
        postRepository.save(find);
    }

    public void unlike(LikeDto likeDto) {
        Post find = postFacade.getPostWithLike(likeDto.getPostId());
        find.getPostLike().removeIf(member -> member.getEmail().equals(likeDto.getEmail()));
        postRepository.save(find);
    }

    public void editPost(PostEditRequest request) {
        Post find = postFacade.getPostWithImages(request.getId());

        find.setIntroduce(request.getIntroduce());
        find.setContent(request.getContent());
        find.setTitle(request.getTitle());
        find.setTags(request.getTags());

        if(request.getProfile() != null) {
            if(find.getProfileImage() != null) {
                imageService.deletePath(find.getProfileImage());
            }
            find.setProfileImage(imageService.getPath(request.getProfile(), "profile"));
        }

        if (request.getImages() != null) {
            imageService.deleteImageList(find.getImages());
            imageService.addImageList(request.getImages(), "static", find);
        }

        postRepository.save(find);
    }

    public List<PostResponse> pagePost(Sort sort, Long page) {
        return postRepository.searchMainPage(sort, page);
    }

    public List<PostResponse> memberPosts(String email) {
        return postRepository.searchMemberPosts(email);
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
