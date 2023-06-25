package com.aclive.codenursery.repository;

import com.aclive.codenursery.entities.CodeProject;
import com.aclive.codenursery.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CodeProjectRepository extends ReactiveMongoRepository<CodeProject, String> {

}
