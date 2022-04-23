package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class ExpiredAccessTokenException extends MasterException {

    public static final MasterException EXCEPTION = new ExpiredAccessTokenException();

    private ExpiredAccessTokenException() {
        super(ErrorCode.EXPIRED_ACCESS_TOKEN);
    }
}
