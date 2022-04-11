package com.onpost.domain.service;

import com.onpost.domain.dto.comment.CommentRequest;
import com.onpost.domain.dto.comment.CommentView;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.repository.CommentQueryRepository;
import com.onpost.domain.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;
    private final MemberQueryRepository memberQueryRepository;

    public CommentView leaveMainComment(CommentRequest commentRequest, Long id) {
        Comment comment = Comment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                ..build();
        Comment result = commentQueryRepository.leave(comment);
        return new CommentView(result);
    }
}
