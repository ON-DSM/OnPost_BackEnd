package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class FileUploadFailedException extends MasterException {

    public static final MasterException EXCEPTION = new FileUploadFailedException();

    private FileUploadFailedException() {
        super(ErrorCode.FILE_UPLOAD_FAILED);
    }
}
