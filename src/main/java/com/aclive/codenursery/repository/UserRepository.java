package com.aclive.codenursery.repository;

import com.aclive.codenursery.entities.User;
import com.aclive.codenursery.models.AggregateDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
