package org.example.managers;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

@Service
public interface IContentNegotiationConfigurerManager {
    void manageConfigure(ContentNegotiationConfigurer configurer);
}
