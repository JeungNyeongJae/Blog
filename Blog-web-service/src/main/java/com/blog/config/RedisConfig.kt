package com.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

/**
 *  @author JeungNyeongJae
 *  @date 2019/11/7
 *
 */
@Configuration
open class RedisConfig {

    @Bean
    open fun redisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate {

        return StringRedisTemplate(connectionFactory)
    }
}
