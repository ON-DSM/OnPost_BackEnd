package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import com.onpost.global.error.exception.PageSortException;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

import static com.onpost.domain.entity.QPost.post;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;
    private final MemberQueryRepository memberQueryRepository;


    public PostResponse createPost(PostRequest postRequest) {
        List<Image> images = imageService.getImageList(postRequest.getImages(), "static");

        Member writer = memberQueryRepository.getPostAndMember(postRequest.getId());

        Post post = Post.builder()
                .postLike(new LinkedHashSet<>())
                .images(images)
                .context(postRequest.getContext())
                .title(postRequest.getTitle())
                .writer(writer)
                .build();

        writer.updatePost(post);

        postQueryRepository.save(post);
        memberQueryRepository.save(writer);

        return new PostResponse(post);
    }

    public PostResponse showPost(Long id) {
        Post find = postQueryRepository.show(id);
        return new PostResponse(find);
    }

    public PostResponse editPost(PostRequest per) {
        Post find = postQueryRepository.show(per.getId());

        if(per.getContext() != null) {
            find.setContext(per.getContext());
        }

        if(per.getTitle() != null) {
            find.setTitle(per.getTitle());
        }

        if(per.getImages() != null) {
            imageService.deleteImageList(find.getImages());
            find.setImages(imageService.getImageList(per.getImages(), "static"));
        }

        postQueryRepository.save(find);

        return new PostResponse(find);
    }

    public List<PostResponse> pagePost(String sort, Long page) {
        OrderSpecifier<?> orderBy;
        switch (sort) {
            case "new":
                orderBy = post.id.desc();
                break;
            case "like":
                orderBy = post.postLike.size().desc();
                break;
            default:
                throw PageSortException.EXCEPTION;
        }
        return postQueryRepository.showPage(orderBy, page);
    }

    public void deletePost(Long id) {
        Post post = postQueryRepository.show(id);
        postQueryRepository.delete(post);
    }
}
