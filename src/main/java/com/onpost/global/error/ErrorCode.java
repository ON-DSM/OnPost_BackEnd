package com.onpost.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN("Invalid token", "GLOBAL-401-1",401),
    EXPIRED_REFRESH_TOKEN("Expired Refresh Token", "GLOBAL-401-2", 401),
    EMAIL_ALREADY_EXISTS("Email Already Exists", "GLOBAL_409-1", 409),

    POST_NOT_FOUND("Post Not Found", "POST-404", 404),
    IMAGE_NOT_FOUND("Image Not Found", "IMAGE-404", 404),
    MEMBER_NOT_FOUND("Member Not Found", "MEMBER-404",404),
    COMMENT_NOT_FOUND("Comment Not Found", "COMMENT-404",404),

    FILE_CONVERSION_FAILED("File Change Failed","GLOBAL-400-1", 400),

    PARAMETER_NOT_CONTAIN("Parameter Not Contain", "GLOBAL-404-3",404),
    HEADER_NOT_CONTAIN("Header Not Contain", "GLOBAL-400-2", 400),
    PASSWORD_NOT_MATCH("Password Not Match",  "GLOBAL-401-4", 401),
    DO_NOT_UNDERSTAND_SORT("Don't Understand Sort", "GLOBAL-404-4",404),

    NEED_EMAIL_CERTIFICATION("Need Email Certification", "GLOBAL-401-5",401);

    private final String message;
    private final String code;
    private final int status;
}
