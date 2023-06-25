package com.aclive.codenursery.service;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.entities.User;
import com.aclive.codenursery.models.request.CodeTextRequest;
import com.aclive.codenursery.models.response.CodeTextResponse;
import com.aclive.codenursery.repository.CodeProjectRepository;
import com.aclive.codenursery.repository.CodeSnippetRepository;
import com.aclive.codenursery.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

    private final String SUCCESS_CODE = "0000";
    private final String SUCCESS_MESSAGE = "Successful";
    private final String ERROR_CODE = "1000";
    private final String ERROR_MESSAGE = "Successful";

    public Mono<CodeTextResponse> saveCode(String customerId, CodeTextRequest codeTextRequest){

            List<CodeSnippet> snippets = buildCodeSnippetsFromRequest(codeTextRequest);
            return snippetRepository.saveAll(snippets)
                .collectList()
                .flatMap(snippetList -> {
                    CodeProject project = buildProjectFromRequest(snippetList,codeTextRequest);
                    return projectRepository.save(project);
                })
                .flatMap(project -> {
                    return userRepository.findById(customerId)
                        .switchIfEmpty(
                            Mono.just(User.builder()
                                .id(customerId)
                                .build())
                        )
                        .flatMap(user -> {
                            List<String> projects = user.getProjectIds();
                            projects.add(project.getId());
                            user.setProjectIds(projects);
                            return userRepository.save(user);
                        })
                        .onErrorResume(throwable -> {
                            log.info("Error while saving user {} ", throwable.getMessage());
                            throwable.printStackTrace();
                            return Mono.just(null);

                        });

            })
            .map(user -> {
                return CodeTextResponse.builder()
                    .statusCode(SUCCESS_CODE)
                    .statusMessage(SUCCESS_MESSAGE)
                    .build();
            })
            .switchIfEmpty(
                Mono.just(CodeTextResponse.builder()
                    .statusCode(ERROR_CODE)
                    .statusMessage(ERROR_MESSAGE)
                    .build())
            )
            .onErrorResume(throwable -> {
                log.info("Error while saving record. {} ", throwable.getMessage());
                throwable.printStackTrace();
                return Mono.just(CodeTextResponse.builder()
                    .statusCode(ERROR_CODE)
                    .statusMessage(throwable.getLocalizedMessage())
                    .build());
            });

    }

    private CodeProject buildProjectFromRequest(List<CodeSnippet> snippets,CodeTextRequest codeTextRequest){
        List<String> snippetIds = snippets.stream().map(snippet -> snippet.getId()).collect(Collectors.toList());
        return CodeProject.builder()
            .id(UUID.randomUUID().toString())
            .language(codeTextRequest.getLanguage())
            .name(codeTextRequest.getProjectName())
            .codeSnippetIds(snippetIds)
            .build();
    }

    private List<CodeSnippet> buildCodeSnippetsFromRequest(CodeTextRequest request){
        List<CodeSnippet> snippets = new ArrayList<>();

        for(int i = 0; i < request.getSnippets().size(); i++){
            CodeSnippet snippet = CodeSnippet.builder()
                .code(request.getSnippets().get(i))
                .index(i)
                .id(UUID.randomUUID().toString())
                .build();
            snippets.add(snippet);
        }

        return snippets;
    }
}
