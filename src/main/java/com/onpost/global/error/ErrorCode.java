package com.onpost.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    FILE_CONVERSION_FAILED("File Change Failed","GLOBAL-400-1", 400),
    HEADER_NOT_CONTAIN("Header Not Contain", "GLOBAL-400-2", 400),
    REQUEST_VALIDATION("", "GLOBAL-400-3", 400),

    INVALID_TOKEN("Invalid Token", "GLOBAL-401-1",401),
    WRONG_TOKEN("Wrong Token", "GLOBAL-401-2", 401),
    EXPIRED_ACCESS_TOKEN("Expired Access Token", "GLOBAL-401-3", 401),
    EXPIRED_REFRESH_TOKEN("Expired Refresh Token", "GLOBAL-401-4", 401),
    UNSUPPORTED_TOKEN("Unsupported Token", "GLOBAL-401-5", 401),
    PASSWORD_NOT_MATCH("Password Not Match",  "GLOBAL-401-6", 401),
    NEED_EMAIL_CERTIFICATION("Need Email Certification", "GLOBAL-401-7",401),
    DIFFERENT_SIGNATURE_TOKEN("Different Signature Token", "GLOBAL-401-8", 401),

    POST_NOT_FOUND("Post Not Found", "POST-404", 404),
    MEMBER_NOT_FOUND("Member Not Found", "MEMBER-404",404),
    COMMENT_NOT_FOUND("Comment Not Found", "COMMENT-404",404),

    PARAMETER_NOT_CONTAIN("Parameter Not Contain", "GLOBAL-404-1",404),
    DO_NOT_UNDERSTAND_SORT("Don't Understand Sort", "GLOBAL-404-2",404),

    EMAIL_ALREADY_EXISTS("Email Already Exists", "GLOBAL-409-1", 409),

    FILE_SIZE_LIMIT("File Size Limit", "GLOBAL-413-1", 413);

    private final String message;
    private final String code;
    private final int status;
}
