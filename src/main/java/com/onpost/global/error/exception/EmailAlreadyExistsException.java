package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class EmailAlreadyExistsException extends MasterException {

    public static final MasterException EXCEPTION = new EmailAlreadyExistsException();

    private EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
