package com.blog.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.misc.Launcher;

import java.io.IOException;
import java.util.Map;

/**
 * @author JeungNyeongJae
 * @date 2019/12/10
 */
@Component("grpcLauncher")
public class GrpcLauncher {

    private Logger logger = LoggerFactory.getLogger(Launcher.class);


    /**
     * 定义Grpc Server
     */
    private Server server;

    /**
     * GRPC 服务启动方法
     */
    public void grpcStart(Map<String, Object> grpcServiceBeanMap) {
        try{
            int grpcServerPort = 50021;
            ServerBuilder serverBuilder = ServerBuilder.forPort(grpcServerPort);
            for (Object bean : grpcServiceBeanMap.values()){
                serverBuilder.addService((BindableService) bean);
                logger.info(bean.getClass().getSimpleName() + " is regist in Spring Boot");
            }
            server = serverBuilder.build().start();
            logger.info("grpc server is started at " + grpcServerPort);
            server.awaitTermination();
            Runtime.getRuntime().addShutdownHook(new Thread(this::grpcStop));
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * GRPC 服务Stop方法
     */
    private void grpcStop(){
        if (server != null){
            server.shutdownNow();
        }
    }
}
