package com.aclive.codenursery.models.response;

import com.aclive.codenursery.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseLogItem {
    private Object response;
    private RequestType operation;
}
