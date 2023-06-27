package com.aclive.codenursery.models.response;

import com.aclive.codenursery.exceptions.CodeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class APIError {
    private String statusCode;
    private String statusMessage;

    public APIError(CodeException exception){
        this.statusCode = exception.getStatusCode();
        this.statusMessage = exception.getMessage();
    }
}
