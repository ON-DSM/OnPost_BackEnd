package com.onpost.global.error;

import com.onpost.global.error.exception.MasterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MasterException.class)
    public ResponseEntity<ErrorResponse> CatchMasterHandler(MasterException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("Error status : {}\nError reason : {}", errorCode.getStatus(), errorCode.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.valueOf(errorCode.getStatus()));
    }
}
