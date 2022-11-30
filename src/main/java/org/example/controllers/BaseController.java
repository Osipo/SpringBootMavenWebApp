package org.example.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;

@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseController {

    ApplicationProperties configuration;

    @Autowired
    public BaseController(ApplicationProperties configuration){
        this.configuration = configuration;
    }
}
