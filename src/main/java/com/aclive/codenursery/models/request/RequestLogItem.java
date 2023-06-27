package com.aclive.codenursery.models.request;

import com.aclive.codenursery.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestLogItem {
    private Object requestBody;
    private String userId;
    private RequestType operation;
    private String projectId;
}
