package com.aclive.codenursery.exceptions;

import com.aclive.codenursery.enums.CanonicalErrorCode;
import com.aclive.codenursery.models.response.APIError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({CodeException.class, Exception.class})
    public final ResponseEntity<APIError> handleException(Exception ex, WebRequest request) {

        log.info("Error :: {}, Message :: {}", ex.getClass().getSimpleName(), ex.getLocalizedMessage());

        var headers = new HttpHeaders();

        if (ex instanceof MissingRequestHeaderException ||
            ex instanceof MissingServletRequestParameterException) {

            log.info("Error header may be missing {}", ex.getLocalizedMessage());

            CanonicalErrorCode canonicalErrorCode = CanonicalErrorCode.MISSING_HEADERS;
            var apiError =
                APIError.builder()
                    .statusCode(canonicalErrorCode.getCanonicalCode())
                    .statusMessage(canonicalErrorCode.getDescription())
                    .build();
            return handleExceptionInternal(apiError, headers, canonicalErrorCode.getHttpStatus());

        }
        else if (ex instanceof CodeException) {
            var genericException = (CodeException) ex;
            return handleExceptionInternal(new APIError(genericException), headers,genericException.getStatus());
        } else {
            CanonicalErrorCode canonicalErrorCode = CanonicalErrorCode.INTERNAL_SERVER_ERROR;
            var apiError =
                APIError.builder()
                    .statusCode(canonicalErrorCode.getCanonicalCode())
                    .statusMessage(canonicalErrorCode.getDescription())
                    .build();
            return handleExceptionInternal(apiError, headers, canonicalErrorCode.getHttpStatus());
        }
    }

    private ResponseEntity<APIError> handleExceptionInternal(
        APIError body, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(body, headers, status);
    }
}
