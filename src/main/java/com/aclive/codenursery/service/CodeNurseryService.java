package com.aclive.codenursery.service;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.entities.User;
import com.aclive.codenursery.exceptions.CodeException;
import com.aclive.codenursery.helpers.RequestToAggregatorDtoMapper;
import com.aclive.codenursery.models.PipelineDataAggregator;
import com.aclive.codenursery.models.request.CodeTextRequest;
import com.aclive.codenursery.models.request.UpdateProjectRequest;
import com.aclive.codenursery.models.response.CodeTextResponse;
import com.aclive.codenursery.repository.CodeProjectRepository;
import com.aclive.codenursery.repository.CodeSnippetRepository;
import com.aclive.codenursery.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeNurseryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodeProjectRepository projectRepository;
    @Autowired
    private CodeSnippetRepository snippetRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CodeSnippetService codeSnippetService;

    @Autowired
    private RequestToAggregatorDtoMapper mapper;

    private final String SUCCESS_CODE = "0000";
    private final String SUCCESS_MESSAGE = "Successful";
    private final String ERROR_CODE = "1000";
    private final String ERROR_MESSAGE = "Successful";

    
    public Mono<CodeTextResponse> saveCodeWithValidation(CodeTextRequest request,String userId){
        //validate request
        PipelineDataAggregator dto = mapper.fromSaveCodeRequest(request,userId);

        return userService.getUserNonEmptyUser(dto)
            .flatMap(projectService::getEmptyProjectWithDto)
            .flatMap(projectService::createProject)
            .flatMap(codeSnippetService::createSnippets)
            .map(this::buildSuccessResponse)
            .onErrorResume(error -> Mono.just(handleException(error)));

    }

    public Mono<CodeTextResponse> updateEditedSnippets(UpdateProjectRequest request,String userId, String projectId){
        //validate request
        PipelineDataAggregator dto = mapper.fromUpdateRequest(request,userId,projectId);

        return userService.getUserNonEmptyUser(dto)
            .flatMap(projectService::getNonEmptyProject)
            .flatMap(codeSnippetService::updateSnippets)
            .map(this::buildSuccessResponse)
            .onErrorResume(error -> Mono.just(handleException(error)));

    }


    private CodeTextResponse  buildSuccessResponse(PipelineDataAggregator dto){
        return  CodeTextResponse.builder()
            .statusCode(SUCCESS_CODE)
            .statusMessage(SUCCESS_MESSAGE)
            .build();
    }

    private CodeTextResponse handleException(Throwable throwable){
        if(throwable instanceof CodeException){
            throw (CodeException)throwable;
        }else{
            throwable.printStackTrace();
            throw new CodeException(throwable);
        }
    }
}
