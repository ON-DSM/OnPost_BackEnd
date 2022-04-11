package com.onpost.domain.service;

import com.onpost.domain.dto.post.PostDto;
import com.onpost.domain.dto.post.PostRequest;
import com.onpost.domain.dto.post.PostResponse;
import com.onpost.domain.dto.post.PostView;
import com.onpost.domain.entity.Image;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import com.onpost.global.error.exception.PageSortException;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.onpost.domain.entity.QPost.post;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class PostService {

    private final PostQueryRepository postQueryRepository;
    private final ImageService imageService;
    private final MemberQueryRepository memberQueryRepository;


    public PostDto createPost(PostRequest postRequest) {
        List<Image> images = imageService.getImageList(postRequest.getImages(), "static");

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

        return new PostDto(post);
    }

    public PostView showPost(Long id) {
        Post find = postQueryRepository.findPostAll(id);
        return new PostView(find);
    }

    public PostView editPost(PostRequest per) {
        Post find = postQueryRepository.findPostAll(per.getId());

        if(per.getContext() != null) {
            find.setContext(per.getContext());
        }

        if(per.getTitle() != null) {
            find.setTitle(per.getTitle());
        }

        postQueryRepository.deletePostImages(find.getImages());

        if(per.getImages() != null) {
            find.setImages(imageService.getImageList(per.getImages(), "static"));
        }

        postQueryRepository.save(find);

        return new PostView(find);
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
        return postQueryRepository.findPage(orderBy, page);
    }

    public void deletePost(Long id) {
        Post post = postQueryRepository.findPostWithWriter(id);
        Member member = memberQueryRepository.findOneWithPost(post.getWriter().getId());
        member.deletePost(post);
        memberQueryRepository.save(member);
        postQueryRepository.delete(post);
    }
}
