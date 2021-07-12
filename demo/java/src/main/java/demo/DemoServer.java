package demo;

import grpc_demo.Demo.HelloReply;
import grpc_demo.Demo.HelloRequest;
import grpc_demo.Demo.NumRequest;
import grpc_demo.Demo.Sum;
import grpc_demo.GreeterGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class DemoServer {

    private static final Logger logger = Logger.getLogger(DemoServer.class.getName());

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    DemoServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });

    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final DemoServer server = new DemoServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        // here are the implementations of the protobuf methods.
        // have a look at here https://grpc.io/docs/languages/java/basics/
        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            System.out.println("coming message: " + req.getName());
            HelloReply reply = HelloReply.newBuilder()
                    .setMessage("Hello " + req.getName() + " from java server").build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void addNum(NumRequest req, StreamObserver<Sum> responseObserver) {
            System.out.println("message coming: " + req.getNum1() +", "+ req.getNum2());
            Sum sum = Sum.newBuilder().setSum(req.getNum1() + req.getNum2()).build();
            responseObserver.onNext(sum);
            responseObserver.onCompleted();
        }
    }
}



