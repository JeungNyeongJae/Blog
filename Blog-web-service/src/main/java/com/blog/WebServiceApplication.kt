package com.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
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
    runApplication<WebServiceApplication>(*args)
}
