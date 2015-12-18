package com.thoughtworks.jimmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootWebApplication.class, args);

        Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEach(System.out::println);

    }
}
