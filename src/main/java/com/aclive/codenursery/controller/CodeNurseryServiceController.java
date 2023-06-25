package com.aclive.codenursery.controller;


import com.aclive.codenursery.enums.RequestType;
import com.aclive.codenursery.helpers.LoggingHelper;
import com.aclive.codenursery.models.request.CodeTextRequest;
import com.aclive.codenursery.models.request.RequestLogItem;
import com.aclive.codenursery.models.request.UpdateProjectRequest;
import com.aclive.codenursery.models.response.CodeTextResponse;
import com.aclive.codenursery.service.CodeNurseryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
public class CodeNurseryServiceController {
    @Autowired
    private CodeNurseryService codeNurseryService;


    private LoggingHelper loggingHelper = LoggingHelper.getInstance();


    @ApiOperation(
        value = "A call to save a code text",
        notes = "The code text will be saved to a user's project",
        response = CodeTextResponse.class
    )
    @PostMapping(value = "/save/{customerId}/code/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CodeTextResponse> saveCodeText(
        @PathVariable String customerId,
        @RequestBody CodeTextRequest codeTextRequest
    ) {
        RequestLogItem requestLogItem = RequestLogItem.builder()
            .customerId(customerId)
            .operation(RequestType.SAVE_CODE)
            .requestBody(codeTextRequest)
            .build();

        loggingHelper.logRequestObject(log, requestLogItem);

        return codeNurseryService.saveCode(customerId, codeTextRequest)
            .map(response -> {
                loggingHelper.logResponseObject(log, RequestType.SAVE_CODE, response);
                return response;
            });

    }

    @ApiOperation(
        value = "A call to edit a snippet within a code",
        notes = "The updated code snippet will be saved to a user's project",
        response = CodeTextResponse.class
    )
    @PatchMapping(value = "/update/{projectId}/snippet/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CodeTextResponse> saveCodeText(
        @PathVariable String projectId,
        @RequestBody UpdateProjectRequest request
        ) {
        RequestLogItem requestLogItem = RequestLogItem.builder()
            .projectId(projectId)
            .operation(RequestType.UPDATE_CODE)
            .requestBody(request)
            .build();

        loggingHelper.logRequestObject(log, requestLogItem);

        return codeNurseryService.saveCode(projectId, request)
            .map(response -> {
                loggingHelper.logResponseObject(log, RequestType.UPDATE_CODE, response);
                return response;
            });

    }
}
