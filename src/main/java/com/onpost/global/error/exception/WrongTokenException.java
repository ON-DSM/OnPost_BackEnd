package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class WrongTokenException extends MasterException {

    public static final MasterException EXCEPTION = new WrongTokenException();

    private WrongTokenException() {
        super(ErrorCode.WRONG_TOKEN);
    }
}
