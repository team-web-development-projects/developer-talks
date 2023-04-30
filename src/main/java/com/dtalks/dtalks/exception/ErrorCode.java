package com.dtalks.dtalks.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_DUPLICATION_ERROR(400, "USER-DUPLICATION", "USER DUPLICATION ERROR"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL-SERVER-ERROR", "INTERNAL SERVER ERROR"),
    AUTHENTICATION_ERROR(401, "AUTHENTICATION-ERROR", "AUTHENTICATION ERROR"),
    VALIDATION_ERROR(400, "VALIDATION-ERROR", "VALIDATION ERROR"),
    USER_NOT_FOUND_ERROR(400, "USER-NOT_FOUND", "USER NOT FOUND ERROR"),
    POST_NOT_FOUND_ERROR(400, "POST-NOT-FOUND", "POST NOT FOUND ERROR"),
    PERMISSION_NOT_GRANTED_ERROR(403, "PERMISSION-NOT-GRANTED", "PERMISSION NOT GRANTED ERROR"),

    EMAIL_ERROR(400, "EMAIL-ERROR", "EMAIL ERROR")
    ;

    private int status;

    private String errorCode;

    private String message;
}
