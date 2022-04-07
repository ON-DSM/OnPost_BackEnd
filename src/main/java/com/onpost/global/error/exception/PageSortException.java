package com.onpost.global.error.exception;

import com.onpost.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class PageSortException extends MasterException {

    public static final MasterException EXCEPTION = new PageSortException();

    private PageSortException() {
        super(ErrorCode.DO_NOT_UNDERSTAND_SORT);
    }
}
