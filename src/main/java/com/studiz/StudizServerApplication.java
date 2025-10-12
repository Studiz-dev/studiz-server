package com.studiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudizServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudizServerApplication.class, args);
    }

}

