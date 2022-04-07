package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class PasswordNotMatchException extends MasterException {

    public static final MasterException EXCEPTION = new PasswordNotMatchException();

    private PasswordNotMatchException() {
        super(ErrorCode.PASSWORD_NOT_MATCH);
    }
}
