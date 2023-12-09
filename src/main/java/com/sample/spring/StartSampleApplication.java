package com.sample.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StartSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartSampleApplication.class, args);
    }
}
