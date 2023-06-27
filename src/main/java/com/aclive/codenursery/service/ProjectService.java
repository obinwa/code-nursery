package com.aclive.codenursery.service;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.exceptions.CodeException;
import com.aclive.codenursery.models.AggregateDto;
import com.aclive.codenursery.repository.CodeProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private  CodeProjectRepository projectRepository;

    public  Mono<AggregateDto> getEmptyProjectWithDto(AggregateDto processingData){
        log.info("processing dto {}",processingData);
        return projectRepository.findById(processingData.getRequest().getProjectName())
            .defaultIfEmpty(CodeProject.builder().build())
            .flatMap(project -> {
                log.info("Project with Id {} ::: {}",processingData.getRequest().getProjectName(),project);
                return StringUtils.isBlank(project.getId()) ? Mono.just(CodeProject.builder().build()) : Mono.empty();
            })
            .switchIfEmpty(Mono.error((new CodeException("1000","Project Exists Already", HttpStatus.BAD_REQUEST))))
            .map(project -> {
                return processingData;
            });

    }

    public  Mono<AggregateDto> createProject(AggregateDto processingData){
        List<String> snippetIds = processingData.getSnippetIds();
        CodeProject project = CodeProject.builder()
            .id(processingData.getRequest().getProjectName())
            .language(processingData.getRequest().getLanguage())
            .name(processingData.getRequest().getProjectName())
            .codeSnippetIds(snippetIds)
            .build();

        return projectRepository.save(project)
            .map(savedProject -> {
                processingData.setProject(savedProject);
                return processingData;
            });
    }

}
