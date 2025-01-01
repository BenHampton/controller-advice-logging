package com.controlleradvicelogging.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "request-response-logger")
@Configuration
@Data
public class RequestProperties {

    private boolean canLog;
}
