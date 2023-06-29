package com.aclive.codenursery.service;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.exceptions.CodeException;
import com.aclive.codenursery.models.PipelineDataAggregator;
import com.aclive.codenursery.repository.CodeProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private  CodeProjectRepository projectRepository;

    public  Mono<PipelineDataAggregator>  getEmptyProjectWithDto(PipelineDataAggregator pipelineDataAggregator){
        log.info("processing dto {}",pipelineDataAggregator);
        return projectRepository.findById(pipelineDataAggregator.getRequest().getProjectName())
            .defaultIfEmpty(CodeProject.builder().build())
            .flatMap(project -> {
                log.info("Project with Id {} ::: {}",pipelineDataAggregator.getRequest().getProjectName(),project);
                return StringUtils.isBlank(project.getId()) ? Mono.just(CodeProject.builder().build()) : Mono.empty();
            })
            .switchIfEmpty(Mono.error((new CodeException("1000","Project Exists Already", HttpStatus.BAD_REQUEST))))
            .map(project -> {
                return pipelineDataAggregator;
            });

    }

    public  Mono<PipelineDataAggregator> createProject(PipelineDataAggregator pipelineDataAggregator){
        CodeProject project = CodeProject.builder()
            .id(pipelineDataAggregator.getRequest().getProjectName())
            .language(pipelineDataAggregator.getRequest().getLanguage())
            .name(pipelineDataAggregator.getRequest().getProjectName())
            .build();

        return projectRepository.save(project)
            .map(savedProject -> {
                pipelineDataAggregator.setProject(savedProject);
                return pipelineDataAggregator;
            });
    }

    public Mono<PipelineDataAggregator> getNonEmptyProject(PipelineDataAggregator pipelineDataAggregator){
        return projectRepository.findById(pipelineDataAggregator.getRequest().getProjectName())
            .switchIfEmpty(Mono.error(new CodeException("1000","Project does not exist", HttpStatus.BAD_REQUEST)))
            .map(project -> {
                pipelineDataAggregator.setProject(project);
                return pipelineDataAggregator;
            })
            .map(response -> (PipelineDataAggregator)response);

    }



}
