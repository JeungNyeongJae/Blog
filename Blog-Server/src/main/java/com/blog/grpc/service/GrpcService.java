package com.blog.grpc.service;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 *  @author JeungNyeongJae
 *  @date 2019/12/10
 *  Class Scan
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GrpcService {
}
