package com.aclive.codenursery.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateProjectRequest {
    private List<CodeSnippetInfo> updatedSnippets;
    private String projectName;
}
