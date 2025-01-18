package com.aclive.codenursery.connector;

import com.aclive.codenursery.configuration.WebClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HttpClientUtil {

    private final WebClientProperties webClientProperties;

    @Autowired
    public HttpClientUtil(WebClientProperties webClientProperties) {
        this.webClientProperties = webClientProperties;
    }

    public HttpClient createHttpClient() throws SSLException {

        HttpClient httpClient = createCoreHttpClientWithConnectionPooling();
        log.info("Otherwise generating Insecure Context : generateInsecureContext");
        httpClient = httpClient.secure(sslContextSpec -> sslContextSpec.sslContext(generateInsecureContext()));


        return httpClient;
    }

    private HttpClient createCoreHttpClientWithConnectionPooling() {
        HttpClient httpClient;
        if (null != webClientProperties && webClientProperties.getResponseTimeout() > 0 && webClientProperties.getConnectTimeout() > 0 && webClientProperties.getReadTimeout() > 0) {
            int connectionTimeout = webClientProperties.getConnectTimeout() * 1000;
            int readTimeout = webClientProperties.getReadTimeout() * 1000;
            Duration responseTimeout = Duration.ofMillis(webClientProperties.getResponseTimeout() * 1000);
            httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .responseTimeout(responseTimeout)
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)))
                .followRedirect(true);
        } else {
            httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)
                .followRedirect(true);
        }

        if (null != webClientProperties && webClientProperties.isEnableWireTap()) {
            httpClient = httpClient.wiretap("reactor.netty.http.client.HttpClient", webClientProperties.getLogLevel(), AdvancedByteBufFormat.TEXTUAL);
        }

        return httpClient;
    }

    public SslContext generateInsecureContext() {
        log.info("Generating Insecure Context Server Certifications : generateInsecureContext");
        try {
            return SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        } catch (SSLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
