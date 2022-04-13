package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class FileConversionException extends MasterException {

    public static final MasterException EXCEPTION = new FileConversionException();

    private FileConversionException() {
        super(ErrorCode.FILE_CONVERSION_FAILED);
    }
}
