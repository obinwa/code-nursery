package com.aclive.codenursery.models.response.compilerResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeXResponse {

    private String timeStamp;
    private BigInteger status;
    private String output;
    private String error;
    private String language;
    private String info;
}
