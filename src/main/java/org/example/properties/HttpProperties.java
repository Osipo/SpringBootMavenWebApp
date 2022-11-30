package org.example.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "server")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component("http_props")
public class HttpProperties {

    int port;
    boolean ignoreAcceptHeader;

    @Value("${server.queryParamAcceptName}")
    String favourParamName;

    @Value("${server.acceptFromQueryParam}")
    boolean favourParam;

    @Value("${server.defaultMediaType}")
    String defaultMediaTypeString;

    @Value("${server.acceptFromExtension}")
    boolean favourParamPathExtension;

    Map<String, Object> error;
}
