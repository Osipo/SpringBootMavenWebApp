package org.example.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationProperties {

    @Value("${miscellaneous.config.name}")
    String name;

    @Autowired
    @Qualifier("db_props")
    DbProperties dbProperties;

    @Autowired
    @Qualifier("http_props")
    HttpProperties httpProperties;

    @Autowired
    @Qualifier("hibernate_props")
    JpaHibernateProperties jpaProperties;
}
