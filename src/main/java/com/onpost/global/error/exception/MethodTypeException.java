package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class MethodTypeException extends MasterException {

    public static final MasterException EXCEPTION = new MethodTypeException();

    private MethodTypeException() {
        super(ErrorCode.DO_NOT_UNDERSTAND_REQUEST);
    }
}
