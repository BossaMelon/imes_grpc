import grpc
import demo_pb2_grpc
import demo_pb2
from concurrent import futures



# service名字影响父类名字
# Greeter名字随便起
class Greeter(demo_pb2_grpc.GreeterServicer):
    # override父类方法
    # 定义每个service功能的具体实现

    def SayHello(self, request, context):
        # 输入参数主要是第二个,此函数在proto定义中有的request只有name字段

        result = "Hey " + request.name
        # 处理输入的name字段
        print('\r',"message comes")
        return demo_pb2.HelloReply(message=result)
        # 返回结果

    def AddNum(self, request, context):
        result = request.num1+request.num2
        return demo_pb2.Sum(sum=result)


def serve():
    # 建立server对象
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    # 注册service对象
    demo_pb2_grpc.add_GreeterServicer_to_server(Greeter(), server)
    # 指定监听ip+port
    server.add_insecure_port('localhost:50051')
    server.start()
    print("Waiting for client...")
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
