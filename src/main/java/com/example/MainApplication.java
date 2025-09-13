package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        new SpringApplication(MainApplication.class).run(args);

        log.info("""
                \s
                ----------------------------------------------------------
                Access URL: http://localhost:8080/swagger-ui/index.html
                ----------------------------------------------------------""");
    }
}