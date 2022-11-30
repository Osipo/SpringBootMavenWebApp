package org.example;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.properties.ApplicationProperties;
import org.example.data.IStringGen;
import org.example.data.StringGenDev;
import org.example.data.StringGenTest;
import org.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfiguration {

    static List<String> supportedContentTypes;
    static{
        supportedContentTypes = new ArrayList<>(10);
        supportedContentTypes.add("application/json");
        supportedContentTypes.add("application/xml");
        supportedContentTypes.add("application/text");
        supportedContentTypes.add("text/json");
        supportedContentTypes.add("text/xml");
        supportedContentTypes.add("text/html");
    }

    @Autowired
    ApplicationProperties config;

    @Bean(name = "supportedMediaTypes")
    public List<String> getSupportedContentTypes(){
        return supportedContentTypes;
    }

    @Bean(name = "str_media_map_with_aliases")
    public IStringToMediaTypeConverter getMediaTypeMap(){
        return new MediaTypeMap();
    }



    @Bean
    public IStringGen getStringGen(){
        if(config.getName().equals("dev"))
            return new StringGenDev();
        else
            return new StringGenTest();
    }
}