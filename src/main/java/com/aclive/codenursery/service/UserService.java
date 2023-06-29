package com.aclive.codenursery.service;

import com.aclive.codenursery.exceptions.CodeException;
import com.aclive.codenursery.models.PipelineDataAggregator;
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

    public Mono<PipelineDataAggregator> getEmptyUserWithDto(PipelineDataAggregator processingData){
        return userRepository.findById(processingData.getRequest().getUserId())
            .map(user -> {
                if(user != null)  return Mono.error(new CodeException("1000","User Exists Already", HttpStatus.BAD_REQUEST));
                return processingData;
            })
            .map(response -> (PipelineDataAggregator)response);

    }

    public Mono<PipelineDataAggregator> getUserNonEmptyUser(PipelineDataAggregator processingData){
        return userRepository.findById(processingData.getRequest().getUserId())
            .switchIfEmpty(Mono.error(new CodeException("1000","User does not exist", HttpStatus.BAD_REQUEST)))
            .map(user -> {
                processingData.setUser(user);
                return processingData;
            })
            .map(response -> (PipelineDataAggregator)response);

    }
}
