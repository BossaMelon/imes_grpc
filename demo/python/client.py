from __future__ import print_function

import time

import grpc

import demo_pb2
import demo_pb2_grpc


def run(ip, port):
    socket = ip + ":" + port
    channel = grpc.insecure_channel(socket)

    stub = demo_pb2_grpc.GreeterStub(channel)

    num1 = 1
    num2 = 2

    while True:
        hello_reply = stub.SayHello(demo_pb2.HelloRequest(name='yuehao'))
        sum_reply = stub.AddNum(demo_pb2.NumRequest(num1=num1, num2=num2))

        print("received: " + hello_reply.message)
        print("received sum = " + str(sum_reply.sum))
        num1 += 1
        num2 += 1

        time.sleep(1)


if __name__ == '__main__':
    ip = "localhost"
    port = "50051"
    run(ip, port)
