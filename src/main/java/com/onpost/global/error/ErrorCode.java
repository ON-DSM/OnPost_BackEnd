package com.onpost.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN("Invalid token", 401),
    EXPIRED_REFRESH_TOKEN("Expired Refresh Token", 401),
    EMAIL_ALREADY_EXISTS("Email Already Exists", 409),

    POST_NOT_FOUND("Post Not Found", 404),
    IMAGE_NOT_FOUND("Image Not Found", 404),
    MEMBER_NOT_FOUND("Member Not Found", 404),

    PASSWORD_NOT_MATCH("Password Not Match", 400),
    DO_NOT_UNDERSTAND_SORT("Don't Understand Sort", 400);

    private final String message;
    private final int status;
}
