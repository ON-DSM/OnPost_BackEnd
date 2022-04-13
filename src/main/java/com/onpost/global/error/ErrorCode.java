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
    COMMENT_NOT_FOUND("Comment Not Found", 404),

    FILE_CONVERSION_FAILED("File Change Failed", 400),

    PARAMETER_NOT_CONTAIN("Parameter Not Contain", 404),
    HEADER_NOT_CONTAIN("Header Not Contain", 400),
    PASSWORD_NOT_MATCH("Password Not Match", 401),
    DO_NOT_UNDERSTAND_SORT("Don't Understand Sort", 404);

    private final String message;
    private final int status;
}
