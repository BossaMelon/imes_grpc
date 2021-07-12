package demo;

import grpc_demo.Demo.HelloReply;
import grpc_demo.Demo.HelloRequest;
import grpc_demo.Demo.NumRequest;
import grpc_demo.Demo.Sum;
import grpc_demo.GreeterGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;

public class DemoClient {
    private static final Logger logger = Logger.getLogger(DemoClient.class.getName());

    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    /** Construct client for accessing HelloWorld server using the existing channel. */
    public DemoClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    /** Say hello to server and try the sum method. */
    public void run() throws InterruptedException {
        String name = "yuehao";
        int num1 = 1;
        int num2 = 2;

        while (true){
            // Hello Request
            HelloRequest request = HelloRequest.newBuilder().setName(name).build();
            HelloReply response;
            try {
                response = blockingStub.sayHello(request);
            } catch (StatusRuntimeException e) {
                logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
                return;
            }
            System.out.println("Received: " + response.getMessage());

            // Number Request
            NumRequest req = NumRequest.newBuilder()
                    .setNum1(num1)
                    .setNum2(num2)
                    .build();
            Sum sum;
            try {
                sum = blockingStub.addNum(req);
            } catch (StatusRuntimeException e) {
                logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
                return;
            }
            System.out.println("Sum: " + sum.getSum());

            TimeUnit.SECONDS.sleep(1);
        }


    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */
    public static void main(String[] args) throws Exception {
        // Access a service running on the local machine on port 50051
        String soket = "localhost:50051";

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(soket)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        try {
            DemoClient client = new DemoClient(channel);
            client.run();
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}