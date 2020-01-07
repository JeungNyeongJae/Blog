package com.blog;

import com.blog.grpc.GrpcLauncher;
import com.blog.grpc.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author JeungNyeongJae
 * @date 2020/1/6
 */
@SpringBootApplication
public class BlogApplication {

    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/public/index.html") final Resource indexHtml) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml));
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run( BlogApplication.class , args );
        Map<String, Object> grpcServiceBeanMap = run.getBeansWithAnnotation(GrpcService.class);
        GrpcLauncher grpcLauncher = run.getBean("grpcLauncher", GrpcLauncher.class);
        grpcLauncher.grpcStart(grpcServiceBeanMap);
    }
}
