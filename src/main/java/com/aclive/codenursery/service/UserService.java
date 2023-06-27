package com.aclive.codenursery.service;

import com.aclive.codenursery.exceptions.CodeException;
import com.aclive.codenursery.models.AggregateDto;
import com.aclive.codenursery.repository.CodeProjectRepository;
import com.aclive.codenursery.repository.CodeSnippetRepository;
import com.aclive.codenursery.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<AggregateDto> getEmptyUserWithDto(AggregateDto processingData){
        return userRepository.findById(processingData.getUserId())
            .map(user -> {
                if(user != null)  return Mono.error(new CodeException("1000","User Exists Already", HttpStatus.BAD_REQUEST));
                return processingData;
            })
            .map(response -> (AggregateDto)response);

    }

    public Mono<AggregateDto> getUserWithDto(AggregateDto processingData){
        return userRepository.findById(processingData.getUserId())
            .switchIfEmpty(Mono.error(new CodeException("1000","User does not exist", HttpStatus.BAD_REQUEST)))
            .map(user -> {
                processingData.setUser(user);
                return processingData;
            })
            .map(response -> (AggregateDto)response);

    }
}
