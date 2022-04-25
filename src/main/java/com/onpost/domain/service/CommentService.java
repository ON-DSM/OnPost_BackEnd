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
import com.onpost.domain.repository.CommentQueryRepository;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import com.onpost.domain.repository.jpa.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PostQueryRepository postQueryRepository;
    private final CommentRepository commentRepository;

    public void saveSub(CommentRequest commentRequest) {
        MainComment parent = commentQueryRepository.findMainById(commentRequest.getParentId());
        SubComment comment = SubComment.builder()
                .content(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .main(parent)
                .build();
        parent.getSubComments().add(comment);

        commentRepository.save(comment);
    }

    public void saveMain(CommentRequest commentRequest) {
        Post post = postQueryRepository.findOneWithComment(commentRequest.getParentId());
        MainComment comment = MainComment.builder()
                .content(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .post(post)
                .build();
        post.getComments().add(comment);

        commentRepository.save(comment);
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentQueryRepository.findMainById(id);
        return new CommentResponse(comment);
    }

    public CommentView editComment(CommentEditRequest commentEditRequest) {
        Comment comment = commentQueryRepository.findOneById(commentEditRequest.getId());
        comment.setContext(commentEditRequest.getContext());
        comment = commentRepository.save(comment);
        return new CommentView(comment);
    }

    public void deleteOne(Long id) {
        Comment comment = commentQueryRepository.findOneById(id);
        deleteComment(comment);
    }

    public void deleteComment(Comment comment) {
        if (comment instanceof MainComment main) {
            Post post = main.getParent_post();
            post.getComments().remove(comment);
        }
        else if(comment instanceof SubComment sub) {
            MainComment mainComment = sub.getMain();
            mainComment.getSubComments().add(sub);
        }

        commentRepository.delete(comment);
    }

    public void deleteWriter(Member member) {
        List<Comment> comments = commentQueryRepository.findAllByWriter(member);
        comments.forEach(this::deleteComment);
    }
}
