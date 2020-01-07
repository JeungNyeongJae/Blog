package com.blog.grpc.client;

import com.Demo;
import com.GreeterGrpc;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.SSLException;

/**
 * @author JeungNyeongJae
 * @date 2019/12/10
 */
@Component
public class GrpcClient {

    @Resource
    private GrpcClientManager grpcClientManager;

    public void call() throws SSLException {
        ManagedChannel channel = grpcClientManager.getChannel();
        Demo.HelloRequest.Builder builder = Demo.HelloRequest.newBuilder();
        builder.setName("World");
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
        Demo.HelloReply helloReply = stub.sayHello(builder.build());
        System.out.println(helloReply.getMessage());
    }
}
