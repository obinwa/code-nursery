package com.aclive.codenursery.models;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.entities.User;
import com.aclive.codenursery.models.request.CodeTextRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AggregateDto {
    private User user;
    private CodeProject project;
    private CodeSnippet snippet;
    private List<String> snippetIds;

    private CodeTextRequest request;
    private String userId;


//    class codeRequest{
//        User user;
//        CodeProject project;
//        Cod
//    }

}
