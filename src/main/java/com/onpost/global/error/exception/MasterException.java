package com.onpost.global.error.exception;


import com.onpost.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MasterException extends RuntimeException {

    private final ErrorCode errorCode;
}
