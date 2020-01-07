package com.blog.grpc.server;

import com.Demo;
import com.GreeterGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @author JeungNyeongJae
 * @date 2019/12/10
 */
public class GrpcServer extends GreeterGrpc.GreeterImplBase{

    @Override
    public void sayHello(Demo.HelloRequest request, StreamObserver<Demo.HelloReply> responseObserver) {
        Demo.HelloReply.Builder builder = Demo.HelloReply.newBuilder();
        builder.setMessage("Hello");
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
