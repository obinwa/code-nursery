package com.aclive.codenursery.configuration;

import io.netty.handler.logging.LogLevel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("web-client")
@Configuration
public class WebClientProperties {

    private int connectTimeout;

    private int readTimeout;

    private int responseTimeout;

    private boolean useConnectionPooling;

    private LogLevel logLevel = LogLevel.INFO;


    public boolean enableWireTap = true;
}
