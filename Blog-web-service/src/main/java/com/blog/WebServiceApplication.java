package com.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author JeungNyeongJae
 */

@SpringBootApplication
@EnableEurekaClient
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run( WebServiceApplication.class , args );
    }
}
