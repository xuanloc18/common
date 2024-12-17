package dev.cxl.iam_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_EXCEPTION(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_OTP(1009, "otp invalid you must login again", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1010, "role existed", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1011, "Permission existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1012, "role not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1013, "Permission not existed", HttpStatus.BAD_REQUEST),
    USER_HAD_ROLE(1014, " user had role", HttpStatus.BAD_REQUEST),
    ROLE_HAD_PERMISSION(1015, "role has permission", HttpStatus.BAD_REQUEST),
    USER_ROLE_NOT_EXISTED(1015, "not exist", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSION_NOT_EXISTED(1015, "not exist", HttpStatus.BAD_REQUEST),
    USER_DIS_ENABLE(1016, "User dis enable", HttpStatus.BAD_REQUEST),
    USER_DELETED(1016, "User deleted", HttpStatus.BAD_REQUEST),
    EMAIL_EXCEPTION(1017, "do not form email", HttpStatus.BAD_REQUEST),
    ROLE_DELETE(1018, "role deleted", HttpStatus.BAD_REQUEST),
    PERMISSION_DELETE(1019, "permission deleted", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
