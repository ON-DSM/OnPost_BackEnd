package com.onpost.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN("Invalid token", 401),
    EMAIL_ALREADY_EXISTS("Email Already Exists", 409);

    private final String message;
    private final int status;
}
