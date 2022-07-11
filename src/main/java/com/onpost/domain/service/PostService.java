package com.onpost.domain.service;

import com.onpost.domain.dto.LikeDto;
import com.onpost.domain.dto.comment.MainCommentResponse;
import com.onpost.domain.dto.member.MemberView;
import com.onpost.domain.dto.post.*;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.Sort;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.facade.PostFacade;
import com.onpost.domain.repository.CommentRepository;
import com.onpost.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostFacade postFacade;
    private final ImageService imageService;
    private final MemberFacade memberFacade;
    private final CommentRepository commentRepository;

    private static final String DEFAULT_IMAGE = System.getenv("POST_DEFAULT");

    public void createPost(PostCreateRequest request) {
        Member writer = memberFacade.getMemberWithPost(request.getEmail());

        Post post = Post.builder()
                .introduce(request.getIntroduce())
                .content(request.getContent())
                .title(request.getTitle())
                .tags(request.getTags())
                .profileImage(DEFAULT_IMAGE)
                .writer(writer)
                .build();

        if(request.getProfile() != null) {
            if(!post.getProfileImage().equals(DEFAULT_IMAGE)) {
                imageService.deletePath(post.getProfileImage());
            }
            post.setProfileImage(imageService.getPath(request.getProfile(), "profile"));
        }

        writer.getMakePost().add(post);

        postRepository.save(post);
    }

    public PostView showPost(Long id, String email) {
        Post find = postFacade.getPostWithAll(id);
        List<MainCommentResponse> comments = commentRepository.findMainByPost(find);
        boolean check = email == null || postRepository.checkLike(email, id);

        return PostView.builder()
                .id(find.getId())
                .title(find.getTitle())
                .content(find.getContent())
                .createAt(find.getCreateAt())
                .comments(comments)
                .profile(find.getProfileImage())
                .introduce(find.getIntroduce())
                .tags(find.getTags())
                .writer(new MemberView(find.getWriter()))
                .like((long) find.getPostLike().size())
                .doLike(check)
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
        Post find = postFacade.getPost(request.getId());

        if(!find.getWriter().getEmail().equals(memberFacade.getEmail())) {
            throw new AccessDeniedException(null);
        }

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

        postRepository.save(find);
    }

    public List<PostResponse> pagePost(Sort sort, Long page) {
        return postRepository.searchMainPage(sort, page);
    }

    public List<PostResponse> memberPosts(String email) {
        return postRepository.searchMemberPosts(email);
    }

    public List<PostTop3Response> top3Post(Sort sort) {
        List<PostResponse> list = postRepository.searchTop3(sort);

        List<PostTop3Response> response = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            response.add(new PostTop3Response(list.get(i), i + 1));
        }

        return response;
    }

    public void deletePost(Long id) {
        Post find = postFacade.getPostWithAll(id);

        if(!find.getWriter().getEmail().equals(memberFacade.getEmail())) {
            throw new AccessDeniedException(null);
        }

        if(!find.getProfileImage().equals(DEFAULT_IMAGE)) {
            imageService.deletePath(find.getProfileImage());
        }

        postRepository.delete(find);
    }

    public List<PostResponse> search(String param) {
        return postRepository.searchPosts(param);
    }
}
