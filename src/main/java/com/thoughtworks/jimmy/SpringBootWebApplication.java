package com.thoughtworks.jimmy;

import com.thoughtworks.jimmy.application.JadeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

@Import(JadeConfig.class)
@SpringBootApplication
public class SpringBootWebApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootWebApplication.class, args);

        Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEach(System.out::println);

    }
}
