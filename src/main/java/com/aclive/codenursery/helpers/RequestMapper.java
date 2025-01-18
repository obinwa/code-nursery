package com.aclive.codenursery.helpers;

import com.aclive.codenursery.models.PipelineDataAggregator;
import com.aclive.codenursery.models.request.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class RequestMapper {


    public PipelineDataAggregator fromUpdateRequest(UpdateProjectRequest updateProjectRequest, String userId, String projectId){
        return PipelineDataAggregator.builder()
            .request(AggregatorRequest.builder()
                .projectName(projectId)
                .userId(userId)
                .updatedSnippets(updateProjectRequest.getUpdatedSnippets())
                .build())
            .build();
    }

    public PipelineDataAggregator fromSaveCodeRequest(CodeTextRequest codeTextRequest, String userId){
        return PipelineDataAggregator.builder()
            .request(AggregatorRequest.builder()
                .projectName(codeTextRequest.getProjectName())
                .language(codeTextRequest.getLanguage())
                .userId(userId)
                .snippets(codeTextRequest.getSnippets())
                .build())
            .build();
    }

    public CompilerBackendRequest fromRunCodeRequest(RunCodeRequest request){
        List<CodeSnippetInfo> sortedSnippets = sortSnippets(request.getSnippets());

        String codeText = mergeCodeSnippets(sortedSnippets);

        CompilerBackendRequest compilerBackendRequest = CompilerBackendRequest.builder()
            .code(codeText)
            .language(request.getLanguage())
            .build();

        return compilerBackendRequest;

    }

    private List<CodeSnippetInfo> sortSnippets(List<CodeSnippetInfo> snippets){
        snippets.sort(this::compareByIndex);
        return snippets;
    }

    private int compareByIndex(CodeSnippetInfo lhs, CodeSnippetInfo rhs) {
        return lhs.getIndex().compareTo(rhs.getIndex());
    }

    private String mergeCodeSnippets(List<CodeSnippetInfo> snippets){
        StringBuilder codeText = new StringBuilder();
        for(CodeSnippetInfo snippet: snippets) {
            codeText.append(snippet.getCode());
        }
        return codeText.toString();
    }

}
