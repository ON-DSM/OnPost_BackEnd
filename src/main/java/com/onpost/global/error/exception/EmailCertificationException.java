package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class EmailCertificationException extends MasterException {

    public static final MasterException EXCEPTION = new EmailCertificationException();

    private EmailCertificationException() {
        super(ErrorCode.NEED_EMAIL_CERTIFICATION);
    }
}
