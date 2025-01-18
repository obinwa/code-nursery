package com.aclive.codenursery.service;

import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.models.PipelineDataAggregator;
import com.aclive.codenursery.models.request.AggregatorRequest;
import com.aclive.codenursery.models.request.CodeSnippetInfo;
import com.aclive.codenursery.repository.CodeSnippetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeSnippetService {
    @Autowired
    private CodeSnippetRepository snippetRepository;

    public Mono<PipelineDataAggregator> createSnippets(PipelineDataAggregator processingData){
        AggregatorRequest request = processingData.getRequest();
        List<CodeSnippet> snippets = new ArrayList<>();

        for(int i = 0; i < request.getSnippets().size(); i++){
            CodeSnippet snippet = CodeSnippet.builder()
                .code(request.getSnippets().get(i))
                .index(new BigDecimal(i))
                .id(UUID.randomUUID().toString())
                .projectId(processingData.getRequest().getProjectName())
                .build();
            snippets.add(snippet);
        }

        return snippetRepository.saveAll(snippets)
            .collectList()
            .map( this::convertToSnippetIds)
            .map(snippetIds -> {
                processingData.setSnippetIds(snippetIds);
                return processingData;
            });

    }

    public Mono<PipelineDataAggregator> updateSnippets(PipelineDataAggregator pipelineDataAggregator){
        return Flux.fromIterable(pipelineDataAggregator.getRequest().getUpdatedSnippets())
            .flatMap(this::editSnippet)
            .map(editedSnipet -> {
                editedSnipet.setProjectId(pipelineDataAggregator.getRequest().getProjectName());
                return editedSnipet;
            })
            .collectList()
            .flatMap(snippets -> {
                return snippetRepository.saveAll(snippets).collectList();
            })
            .map(list ->  pipelineDataAggregator);

    }

    private CodeSnippet buildSnippet(CodeSnippetInfo editedSnippet){
       return CodeSnippet.builder()
            .code(editedSnippet.getCode())
            .index(editedSnippet.getIndex())
            .id(UUID.randomUUID().toString())
            .build();
    }

    private Mono<CodeSnippet> editSnippet(CodeSnippetInfo editedSnippet){
        return snippetRepository.findByIndex(editedSnippet.getIndex())
            .switchIfEmpty(Mono.just(CodeSnippet.builder()
                .code(editedSnippet.getCode())
                .index(editedSnippet.getIndex())
                .id(UUID.randomUUID().toString())
                .build()))
            .map(snippet ->{
                snippet.setCode(editedSnippet.getCode());
                return snippet;
            });


    }

    private List<String> convertToSnippetIds(List<CodeSnippet> snippets){
        return snippets.stream().map(snippet -> snippet.getId()).collect(Collectors.toList());
    }
}
