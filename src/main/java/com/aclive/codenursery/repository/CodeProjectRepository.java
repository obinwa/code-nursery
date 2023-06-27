package com.aclive.codenursery.repository;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.entities.User;
import com.aclive.codenursery.models.AggregateDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CodeProjectRepository extends ReactiveMongoRepository<CodeProject, String> {

    Flux<CodeProject> findByNameContaining(String name);

    Mono<CodeProject> findByName(String name);



}
