package org.example.managers;

import org.example.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import java.util.List;

@Component("cncmanager_simple")
public class ContentNegotiationConfigurerManager implements IContentNegotiationConfigurerManager {

    @Autowired
    ApplicationProperties http_config_options;

    //uses above beans.
    @Autowired
    @Qualifier("query_request_default_strategy")
    ContentNegotiationStrategy strategy;

    //called from WebConfig.configureContentNegotiation(configurer)
    public void manageConfigure(ContentNegotiationConfigurer configurer){
        configurer
                .ignoreAcceptHeader(http_config_options.getHttpProperties().isIgnoreAcceptHeader())
                .parameterName(http_config_options.getHttpProperties().getFavourParamName())
                .favorParameter(http_config_options.getHttpProperties().isFavourParam())
                .favorPathExtension(http_config_options.getHttpProperties().isFavourParamPathExtension())
                .useJaf(false);

        configurer.strategies(List.of(strategy)); //strategy set defaultMediaType.
    }
}
