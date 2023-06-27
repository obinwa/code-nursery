package com.aclive.codenursery.service;

import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.models.AggregateDto;
import com.aclive.codenursery.models.request.CodeTextRequest;
import com.aclive.codenursery.repository.CodeProjectRepository;
import com.aclive.codenursery.repository.CodeSnippetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeSnippetService {
    @Autowired
    private CodeSnippetRepository snippetRepository;

    public Mono<AggregateDto> createSnippets(AggregateDto processingData){
        CodeTextRequest request = processingData.getRequest();
        List<CodeSnippet> snippets = new ArrayList<>();

        for(int i = 0; i < request.getSnippets().size(); i++){
            CodeSnippet snippet = CodeSnippet.builder()
                .code(request.getSnippets().get(i))
                .index(i)
                .id(UUID.randomUUID().toString())
                .build();
            snippets.add(snippet);
        }

        return snippetRepository.saveAll(snippets)
            .collectList()
            .map( snippetList -> {
                return convertToSnippetIds(snippetList);
            })
            .map(snippetIds -> {
                processingData.setSnippetIds(snippetIds);
                return processingData;
            });

    }

    private List<String> convertToSnippetIds(List<CodeSnippet> snippets){
        return snippets.stream().map(snippet -> snippet.getId()).collect(Collectors.toList());
    }
}
