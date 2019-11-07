package com.blog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@SpringBootApplication
@EnableEurekaClient

open class WebServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(WebServiceApplication::class.java, *args)
}
