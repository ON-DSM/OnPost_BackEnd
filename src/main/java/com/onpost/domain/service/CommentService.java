package com.onpost.domain.service;

import com.onpost.domain.dto.comment.CommentEditRequest;
import com.onpost.domain.dto.comment.CommentRequest;
import com.onpost.domain.dto.comment.CommentResponse;
import com.onpost.domain.dto.comment.CommentView;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.comment.SubComment;
import com.onpost.domain.entity.member.Member;
import com.onpost.domain.facade.CommentFacade;
import com.onpost.domain.facade.MemberFacade;
import com.onpost.domain.facade.PostFacade;
import com.onpost.domain.repository.CommentRepository;
import com.onpost.global.annotation.ServiceSetting;
import lombok.RequiredArgsConstructor;

@ServiceSetting
@RequiredArgsConstructor
public class CommentService {

    private final MemberFacade memberFacade;
    private final PostFacade postFacade;
    private final CommentRepository commentRepository;
    private final CommentFacade commentFacade;

    public void saveSub(CommentRequest commentRequest) {
        MainComment parent = commentFacade.getMainById(commentRequest.getParentId());
        SubComment comment = SubComment.builder()
                .content(commentRequest.getContext())
                .writer(memberFacade.getMember(commentRequest.getWriterId()))
                .main(parent)
                .build();
        parent.getSubComments().add(comment);

        commentRepository.save(comment);
    }

    public void saveMain(CommentRequest commentRequest) {
        Post post = postFacade.getPostWithComment(commentRequest.getParentId());
        MainComment comment = MainComment.builder()
                .content(commentRequest.getContext())
                .writer(memberFacade.getMember(commentRequest.getWriterId()))
                .post(post)
                .build();
        post.getComments().add(comment);

        commentRepository.save(comment);
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentFacade.getMainById(id);
        return new CommentResponse(comment);
    }

    public CommentView editComment(CommentEditRequest commentEditRequest) {
        Comment comment = commentFacade.getOneById(commentEditRequest.getId());
        comment.setContext(commentEditRequest.getContext());
        comment = commentRepository.save(comment);
        return new CommentView(comment);
    }

    public void deleteOne(Long id) {
        commentRepository.deleteById(id);
    }

    public void deleteWriter(Member member) {
        commentRepository.deleteCommentByWriter(member);
    }
}
