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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PostQueryRepository postQueryRepository;

    public void leaveSubComment(CommentRequest commentRequest) {
        SubComment comment = SubComment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .build();

        MainComment parent = commentQueryRepository.findMain(commentRequest.getParentId());
        parent.getSubComments().add(comment);
        commentQueryRepository.leave(comment);

        commentQueryRepository.leave(parent);
    }

    public void leaveComment(CommentRequest commentRequest) {
        MainComment comment = MainComment.builder()
                .context(commentRequest.getContext())
                .writer(memberQueryRepository.findMember(commentRequest.getWriterId()))
                .build();

        Post post = postQueryRepository.findPostWithComment(commentRequest.getParentId());
        post.getComments().add(comment);
        commentQueryRepository.leave(comment);

        postQueryRepository.save(post);
    }

    public CommentResponse showMain(Long id) {
        MainComment comment = commentQueryRepository.findMain(id);
        return new CommentResponse(comment);
    }

    public CommentView editComment(CommentEditRequest commentEditRequest) {
        Comment comment = commentQueryRepository.findComment(commentEditRequest.getId());
        comment.setContext(commentEditRequest.getContext());
        comment = commentQueryRepository.leave(comment);
        return new CommentView(comment);
    }

    public void deleteComment(Long id, Long parent) {
        Comment comment = commentQueryRepository.findComment(id);
        if(comment instanceof SubComment) {
            deleteSubComment(comment, parent);
        }
        else {
            deleteMainComment((MainComment) comment, parent);
        }
    }

    private void deleteMainComment(MainComment comment, Long parent) {
        Post post = postQueryRepository.findPostWithComment(parent);

        post.getComments().remove(comment);
        postQueryRepository.save(post);

        comment.getSubComments().forEach(commentQueryRepository::delete);
        commentQueryRepository.delete(comment);
    }

    private void deleteSubComment(Comment comment, Long parent) {
        MainComment main = commentQueryRepository.findMain(parent);

        main.getSubComments().remove(comment);
        commentQueryRepository.leave(main);

        commentQueryRepository.delete(comment);
    }
}
