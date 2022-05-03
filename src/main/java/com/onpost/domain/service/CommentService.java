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
import com.onpost.domain.repository.query.MemberRepository;
import com.onpost.domain.repository.query.PostRepository;
import com.onpost.domain.repository.CommentRepository;
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

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void saveSub(CommentRequest commentRequest) {
        MainComment parent = commentRepository.findMainById(commentRequest.getParentId());
        SubComment comment = SubComment.builder()
                .content(commentRequest.getContext())
                .writer(memberRepository.findMember(commentRequest.getWriterId()))
                .main(parent)
                .build();
        parent.getSubComments().add(comment);

        commentRepository.save(comment);
    }

    public void saveMain(CommentRequest commentRequest) {
        Post post = postRepository.findOneWithComment(commentRequest.getParentId());
        MainComment comment = MainComment.builder()
                .content(commentRequest.getContext())
                .writer(memberRepository.findMember(commentRequest.getWriterId()))
                .post(post)
                .build();
        post.getComments().add(comment);

        commentRepository.save(comment);
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentRepository.findMainById(id);
        return new CommentResponse(comment);
    }

    public CommentView editComment(CommentEditRequest commentEditRequest) {
        Comment comment = commentRepository.findOneById(commentEditRequest.getId());
        comment.setContext(commentEditRequest.getContext());
        comment = commentRepository.save(comment);
        return new CommentView(comment);
    }

    public void deleteOne(Long id) {
        Comment comment = commentRepository.findOneById(id);
        deleteComment(comment);
    }

    public void deleteComment(Comment comment) {
        if (comment instanceof MainComment main) {
            Post post = main.getParent_post();
            post.getComments().remove(main);
        }
        else if(comment instanceof SubComment sub) {
            MainComment mainComment = sub.getMain();
            mainComment.getSubComments().add(sub);
        }

        commentRepository.delete(comment);
    }

    public void deleteWriter(Member member) {
        List<Comment> comments = commentRepository.findAllByWriter(member);
        comments.forEach(this::deleteComment);
    }
}
