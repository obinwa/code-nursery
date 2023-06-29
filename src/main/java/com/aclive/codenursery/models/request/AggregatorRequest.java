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
public class AggregatorRequest {
    private List<String> snippets;
    private String language;
    private String projectName;
    private String userId;
    private String snippetIndex;
    private List<CodeSnippetEditInfo> updatedSnippets;
}
