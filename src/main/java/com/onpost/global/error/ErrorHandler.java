package com.onpost.global.error;

import com.onpost.global.error.exception.MasterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MasterException.class)
    public ResponseEntity<ErrorResponse> masterExceptions(MasterException e) {
        ErrorCode errorCode = e.getErrorCode();
        return result(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(BindException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ErrorCode.REQUEST_VALIDATION.getCode(), ErrorCode.REQUEST_VALIDATION.getStatus());
        return result(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleParameterExceptions(){
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.PARAMETER_NOT_CONTAIN);
        return result(errorResponse);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleHeaderExceptions() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.HEADER_NOT_CONTAIN);
        return result(errorResponse);
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleFileSizeLimitsExceptions() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.FILE_SIZE_LIMIT);
        return result(errorResponse);
    }

    private ResponseEntity<ErrorResponse> result(ErrorResponse errorResponse) {
        log.error("Error code : {}\nError reason : {}", errorResponse.getCode(), errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
}
