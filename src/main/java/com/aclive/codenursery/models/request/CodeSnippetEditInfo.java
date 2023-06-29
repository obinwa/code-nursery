package com.aclive.codenursery.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeSnippetEditInfo {
    private String id;
    private String code;
    private BigDecimal index;
    private boolean isNew;
}
