package com.aclive.codenursery.connector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WebHttpClient {

    @Autowired
    private static HttpClientUtil httpClientUtil;
    private static WebClient webClient;

    @Autowired
    public WebHttpClient() {
        try {
            this.webClient = createWebClient();
        }catch (Exception exception){
            this.webClient = WebClient.builder().build();
        }
    }

    private static WebClient createWebClient() throws SSLException {

        HttpClient httpClient = httpClientUtil.createHttpClient()
            .keepAlive(true);
        return WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(1024 * 1024 * 50))
            .build();
    }

    public Mono<Object> post(String endpoint, Object request, Map<String, String> headers, Class responseClass) throws SSLException {

        return webClient
            .post()
            .uri(endpoint)
            .body(BodyInserters.fromValue(request))
            .headers(httpHeaders -> {
                httpHeaders.setAccept(getAcceptedMediaTypes());
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                if (null != headers)
                    headers.keySet().forEach(key -> httpHeaders.add(key, headers.get(key)));
            })
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    log.error("HttpStatusCode = {}", clientResponse.statusCode());
                    log.error("HttpHeaders = {}", clientResponse.headers().asHttpHeaders());
                    return clientResponse.createException()
                        .flatMap(Mono::error);
                }
                return clientResponse.bodyToMono(responseClass);
            });
    }

    private List<MediaType> getAcceptedMediaTypes() {
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        return mediaTypes;
    }
}
