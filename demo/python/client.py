from __future__ import print_function
import grpc
import demo_pb2
import demo_pb2_grpc
import time



def run():
    # ip = ipcheck.get_host_ip()
    ip = "localhost"
    port = "50051"
    socket = ip+":"+port
    channel = grpc.insecure_channel(socket)

    stub = demo_pb2_grpc.GreeterStub(channel)
    # 新建client对象

    while True:
        response = stub.SayHello(demo_pb2.HelloRequest(name='yuehao'))
        # 对象.service对应功能(message文件.对应message(字段赋值))
        # 类似调用service的函数
        response2 = stub.AddNum(demo_pb2.NumRequest(num1=1,num2=10))

        print("received: " + response.message)
        print("Sum = " + str(response2.sum))

        time.sleep(1)


if __name__ == '__main__':
    run()
