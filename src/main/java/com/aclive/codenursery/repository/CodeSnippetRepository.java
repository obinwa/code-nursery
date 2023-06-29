package com.aclive.codenursery.repository;

import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CodeSnippetRepository extends ReactiveMongoRepository<CodeSnippet, String> {

    Mono<CodeSnippet> findByIndex(BigDecimal index);
}
