package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class UnsupportedTokenException extends MasterException {

    public static final MasterException EXCEPTION = new UnsupportedTokenException();

    private UnsupportedTokenException() {
        super(ErrorCode.UNSUPPORTED_TOKEN);
    }
}
