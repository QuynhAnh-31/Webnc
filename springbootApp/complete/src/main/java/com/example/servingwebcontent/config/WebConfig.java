package com.example.servingwebcontent.config;

import com.example.servingwebcontent.converter.UserConverter;
import com.example.servingwebcontent.converter.BookConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserConverter userConverter;

    @Autowired
    private BookConverter bookConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(userConverter);
        registry.addConverter(bookConverter);
    }
}