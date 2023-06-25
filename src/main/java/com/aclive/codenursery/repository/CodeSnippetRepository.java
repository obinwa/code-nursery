package com.aclive.codenursery.repository;

import com.aclive.codenursery.entities.CodeSnippet;
import com.aclive.codenursery.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CodeSnippetRepository extends ReactiveMongoRepository<CodeSnippet, String> {

}
