package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class InvalidTokenException extends MasterException {

    public final static MasterException EXCEPTION = new InvalidTokenException();
    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
