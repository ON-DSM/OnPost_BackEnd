package com.onpost.domain.service;

import com.onpost.domain.dto.comment.CommentRequest;
import com.onpost.domain.dto.comment.CommentResponse;
import com.onpost.domain.dto.comment.CommentView;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.comment.SubComment;
import com.onpost.domain.repository.CommentQueryRepository;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PostQueryRepository postQueryRepository;

    public CommentView leaveSubComment(CommentRequest commentRequest) {
        SubComment comment = SubComment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .build();

        MainComment parent = commentQueryRepository.findParent(commentRequest.getParentId());
        parent.getSubComments().add(comment);
        comment = commentQueryRepository.subLeave(comment);

        commentQueryRepository.mainLeave(parent);
        return new CommentView(comment);
    }

    public CommentView leaveComment(CommentRequest commentRequest) {
        MainComment comment = MainComment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .build();

        Post post = postQueryRepository.findPostWithComment(commentRequest.getParentId());
        post.getComments().add(comment);
        comment = commentQueryRepository.mainLeave(comment);

        postQueryRepository.save(post);
        return new CommentView(comment);
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentQueryRepository.findParent(id);
        return new CommentResponse(comment);
    }
}
