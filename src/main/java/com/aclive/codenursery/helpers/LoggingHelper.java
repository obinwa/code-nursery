package com.aclive.codenursery.helpers;

import com.aclive.codenursery.enums.RequestType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import com.aclive.codenursery.models.request.RequestLogItem;
import com.aclive.codenursery.models.response.ResponseLogItem;


public class LoggingHelper {

    private static LoggingHelper instance;
    @Value("${logging.config:#{null}}")
    private String loggingConfig;
    private ObjectMapper objectMapper;

    private LoggingHelper() {
        objectMapper = new ObjectMapper();
    }

    public static LoggingHelper getInstance() {
        if (instance == null) {
            instance = new LoggingHelper();
        }

        return instance;
    }

    public void logRequestObject(
        Logger logger, RequestType requestType, String customerId,  Object apiRequest
    ) {
        requestObject(
                logger,
                RequestLogItem.builder()
                        .customerId(customerId)
                        .requestBody(apiRequest)
                        .operation(requestType)
                        .build()
        );
    }

    public void logRequestObject(Logger logger, RequestType requestType, Object apiRequest) {
        requestObject(
                logger,
                RequestLogItem.builder()
                        .requestBody(apiRequest)
                        .operation(requestType)
                        .build()
        );
    }

    public void logRequestObject(Logger logger, RequestLogItem requestLogItem) {
        requestObject(
                logger,
                requestLogItem
        );
    }


    public void logResponseObject(Logger logger, RequestType requestType, Object response) {

        ResponseLogItem responseLogItem = ResponseLogItem.builder()
                .operation(requestType)
                .response(response)
                .build();

        responseObject(logger, responseLogItem);
    }

    private void responseObject(Logger logger, Object object) {
        String x = "{}";
        try {
            x = objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (StringUtils.isBlank(loggingConfig)) {
            logger.info("{ \"responseObject\" : {} }", x);
        } else {
           // logger.info(Markers.appendRaw("responseObject", x), null);
        }

    }

    private void requestObject(Logger logger, Object object) {
        String x = "{}";
        try {
            x = objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (StringUtils.isBlank(loggingConfig)) {
            logger.info("{ \"requestObject\" : {} }", x);
        } else {
            //logger.info(Markers.appendRaw("requestObject", x), null);
        }
    }

}