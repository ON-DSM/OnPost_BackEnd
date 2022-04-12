package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class CommentNotFoundException extends MasterException {

    public static final MasterException EXCEPTION = new CommentNotFoundException();

    private CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
