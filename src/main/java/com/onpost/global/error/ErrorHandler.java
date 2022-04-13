package com.onpost.global.error;

import com.onpost.global.error.exception.MasterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        log.error("Error status : {}\nError reason : {}", errorCode.getStatus(), errorCode.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e){
        String[] message = e.getMessage().split("default message");
        ErrorResponse errorResponse = new ErrorResponse(message[message.length - 1].replaceAll("[\\[|\\]]", "").trim(), 400);
        log.error("Error status : {}\nError reason : {}", 400, errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleParameterExceptions(MissingServletRequestParameterException e){
        ErrorResponse response = new ErrorResponse(ErrorCode.PARAMETER_NOT_CONTAIN);
        log.error("message : {}, parameterName : {}", e.getMessage(), e.getParameterName());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleHeaderExceptions(MissingRequestHeaderException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.HEADER_NOT_CONTAIN);
        log.error("message : {}, header : {}", e.getMessage(), e.getHeaderName());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleRequestExceptions(BindException e) {
        String[] message = e.getMessage().split("default message");
        ErrorResponse errorResponse = new ErrorResponse(message[message.length - 1].replaceAll("[\\[|\\]]", "").trim(), 400);
        log.error("Error status : {}\nError reason : {}", 400, errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(400));
    }
}
