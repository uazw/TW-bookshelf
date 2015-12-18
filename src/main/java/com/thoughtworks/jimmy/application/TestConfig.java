package com.thoughtworks.jimmy.application;

import com.thoughtworks.jimmy.service.BookService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestConfig {

    @Bean
    @Profile("test")
    public BookService bookService() {
        return Mockito.mock(BookService.class);
    }
}