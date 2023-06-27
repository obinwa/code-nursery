package com.aclive.codenursery.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CanonicalErrorCode {
    OK("SUCCESS", "0000", "Success", HttpStatus.OK),
    MISSING_HEADERS("BAD_REQUEST", "4000", "Request has missing headers", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(
        "ERROR_CAUSE_UNSPECIFIED", "3001",
        "An internal server error occurred and processing could not be completed.",
        HttpStatus.INTERNAL_SERVER_ERROR),
    TIMEOUT("TIMEOUT", "3003", "The request timed out", HttpStatus.REQUEST_TIMEOUT);
    /*The DPA returns a 404 NOT_FOUND error code indicating to GTAF that the user key is invalid (i.e., non-existing user key) with INVALID_NUMBER.*/

    CanonicalErrorCode(
        final String statusCode, final String canonicalCode, final String description, final HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.canonicalCode = canonicalCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    private String statusCode;
    private String canonicalCode;
    private String description;
    private HttpStatus httpStatus;

    @JsonValue
    public String getStatusCode() {
        return statusCode;
    }

    @JsonValue
    public String getCanonicalCode() {
        return canonicalCode;
    }

    public static CanonicalErrorCode getCanonicalErrorCode(String statusCode) {
        for (CanonicalErrorCode canonicalErrorCode : CanonicalErrorCode.values()) {
            if (canonicalErrorCode.canonicalCode.equals(statusCode)) {
                return canonicalErrorCode;
            }
        }
        return null;
    }
}
