package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class SignatureTokenException extends MasterException {

    public static final MasterException EXCEPTION = new SignatureTokenException();

    private SignatureTokenException() {
        super(ErrorCode.DIFFERENT_SIGNATURE_TOKEN);
    }
}
