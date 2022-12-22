package com.rostik.andrusiv.mesaging;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.UUID;

@SpringBootApplication
@EnableElasticsearchRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        MDC.put("MESSAGE_ID", UUID.randomUUID().toString());
    }

}
