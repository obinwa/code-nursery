package com.aclive.codenursery.helpers;

import com.aclive.codenursery.models.PipelineDataAggregator;
import com.aclive.codenursery.models.request.AggregatorRequest;
import com.aclive.codenursery.models.request.CodeTextRequest;
import com.aclive.codenursery.models.request.UpdateProjectRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestToAggregatorDtoMapper {

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
}
