package org.example;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.controllers.interceptors.SecurityContextAsyncInterceptor;
import org.example.managers.IContentNegotiationConfigurerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;


@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter implements WebMvcConfigurer {


    @Autowired
    @Qualifier("cncmanager_simple")
    IContentNegotiationConfigurerManager configurerManager;

    @Autowired
    @Qualifier("security_context_async_interceptor")
    SecurityContextAsyncInterceptor async_interceptor;

    @Autowired
    @Qualifier("thread_pool_task_executor")
    AsyncTaskExecutor async_executor;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurerManager.manageConfigure(configurer);
    }



    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

        //registrate xml converter.
        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();
        XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
        xmlConverter.setMarshaller(xstreamMarshaller);
        xmlConverter.setUnmarshaller(xstreamMarshaller);
        messageConverters.add(xmlConverter);

        //registrate json converter with date format and indent_output
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        jsonConverter.setObjectMapper(
                new Jackson2ObjectMapperBuilder()
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                        .dateFormat(df)
                        .build()
        );
        messageConverters.add(jsonConverter); //default json converter.
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(async_interceptor)
                .addPathPatterns("/**");
    }


    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer
                .setTaskExecutor(async_executor)
                .registerDeferredResultInterceptors(async_interceptor);
    }

}