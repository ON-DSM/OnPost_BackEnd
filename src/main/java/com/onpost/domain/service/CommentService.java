package com.onpost.domain.service;

import com.onpost.domain.dto.comment.CommentEditRequest;
import com.onpost.domain.dto.comment.CommentRequest;
import com.onpost.domain.dto.comment.CommentResponse;
import com.onpost.domain.dto.comment.CommentView;
import com.onpost.domain.entity.Post;
import com.onpost.domain.entity.comment.Comment;
import com.onpost.domain.entity.comment.MainComment;
import com.onpost.domain.entity.comment.SubComment;
import com.onpost.domain.repository.CommentQueryRepository;
import com.onpost.domain.repository.MemberQueryRepository;
import com.onpost.domain.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PostQueryRepository postQueryRepository;

    public void saveSub(CommentRequest commentRequest) {
        SubComment comment = SubComment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .parent(commentRequest.getParentId())
                .build();

        MainComment parent = commentQueryRepository.findMainById(commentRequest.getParentId());
        parent.getSubComments().add(comment);
        commentQueryRepository.save(comment);

        commentQueryRepository.save(parent);
    }

    public void saveMain(CommentRequest commentRequest) {
        MainComment comment = MainComment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .parent(commentRequest.getParentId())
                .build();

        Post post = postQueryRepository.findOneWithComment(commentRequest.getParentId());
        post.getComments().add(comment);
        commentQueryRepository.save(comment);

        postQueryRepository.save(post);
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentQueryRepository.findMainById(id);
        return new CommentResponse(comment);
    }

    public CommentView editComment(CommentEditRequest commentEditRequest) {
        Comment comment = commentQueryRepository.findOneById(commentEditRequest.getId());
        comment.setContext(commentEditRequest.getContext());
        comment = commentQueryRepository.save(comment);
        return new CommentView(comment);
    }

    public void deleteComment(Long id) {
        Comment comment = commentQueryRepository.findOneById(id);
        if (comment instanceof MainComment main) {

            Post post = postQueryRepository.findOneWithComment(comment.getParent());
            post.getComments().remove(comment);
            postQueryRepository.save(post);

            deleteMain(main);
            return;
        }
        MainComment main = commentQueryRepository.findMainById(comment.getParent());
        main.getSubComments().remove(comment);
        commentQueryRepository.save(main);

        deleteSub(comment);
    }

    public void deleteMain(MainComment comment) {
        Set<SubComment> subComments = Set.copyOf(comment.getSubComments());
        comment.getSubComments().clear();
        commentQueryRepository.delete(comment);

        subComments.forEach(commentQueryRepository::delete);
    }

    public void deleteSub(Comment comment) {
        commentQueryRepository.delete(comment);
    }

    public void deleteWriter(Long memberId) {
        List<Comment> comments = commentQueryRepository.findAllByWriter(memberId);
        comments.forEach(comment -> deleteComment(comment.getId()));
    }
}
