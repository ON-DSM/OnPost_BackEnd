package com.onpost.domain.service;

import com.onpost.domain.dto.comment.CommentCreateRequest;
import com.onpost.domain.dto.comment.CommentEditRequest;
import com.onpost.domain.dto.comment.CommentResponse;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.comment.SubComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.CommentFacade;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.facade.PostFacade;
import com.onpost.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor
public class CommentService {

    private final MemberFacade memberFacade;
    private final PostFacade postFacade;
    private final CommentRepository commentRepository;
    private final CommentFacade commentFacade;

    public Long saveSub(CommentCreateRequest commentRequest) {
        MainComment parent = commentFacade.getMainById(commentRequest.getParentId());
        SubComment comment = SubComment.builder()
                .content(commentRequest.getContent())
                .writer(memberFacade.getMemberByEmail(commentRequest.getEmail()))
                .main(parent)
                .build();
        parent.getSubComments().add(comment);

        return commentRepository.save(comment).getId();
    }

    public Long saveMain(CommentCreateRequest commentRequest) {
        Post post = postFacade.getPostWithComment(commentRequest.getParentId());
        MainComment comment = MainComment.builder()
                .content(commentRequest.getContent())
                .writer(memberFacade.getMemberByEmail(commentRequest.getEmail()))
                .post(post)
                .build();
        post.getComments().add(comment);

        return commentRepository.save(comment).getId();
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentFacade.getMainById(id);
        return new CommentResponse(comment);
    }

    public void editComment(CommentEditRequest commentEditRequest) {
        Comment comment = commentFacade.getOneById(commentEditRequest.getId());
        comment.setContext(commentEditRequest.getContent());
        commentRepository.save(comment);
    }

    public void deleteOne(Long id) {
        commentRepository.deleteById(id);
    }

    public void deleteWriter(Member member) {
        commentRepository.deleteCommentByWriter(member);
    }
}
