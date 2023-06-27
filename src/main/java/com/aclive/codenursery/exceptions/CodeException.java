package com.aclive.codenursery.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeException extends RuntimeException{
    private  String statusCode;
    private String message = "Operation failed";
    private HttpStatus status;

    public CodeException(Throwable throwable){
        this.statusCode = "300";
        this.message = throwable.getLocalizedMessage();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
