package com.aclive.codenursery.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeTextResponse {
    private String statusCode;
    private String statusMessage;
}
