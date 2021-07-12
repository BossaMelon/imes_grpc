from concurrent import futures

import grpc

import demo_pb2
import demo_pb2_grpc


class Greeter(demo_pb2_grpc.GreeterServicer):
    # here are the implementations of the protobuf methods
    def SayHello(self, request, context):

        result = "Hey " + request.name + " from python server"
        print("coming message: "+request.name)
        return demo_pb2.HelloReply(message=result)

    def AddNum(self, request, context):
        result = request.num1 + request.num2
        print("coming message: "+str(request.num1)+", "+str(request.num2))
        return demo_pb2.Sum(sum=result)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    demo_pb2_grpc.add_GreeterServicer_to_server(Greeter(), server)
    server.add_insecure_port('localhost:50051')
    server.start()
    print("Waiting for client...")
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
