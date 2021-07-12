# GRPC


## demo
Create a server client demo with gRPC using java and python
### Python Demo
python: https://grpc.io/docs/languages/python/quickstart/

1. install dependency

		python -m pip install grpcio
		python -m pip install grpcio-tools

2. define protobuf(.proto)

		syntax = "proto3";
		package grpc_demo;

		service FirstGreeter {
		  rpc SayHello (HelloRequest) returns (HelloReply) {}
		}

		message HelloRequest {
		  string name = 1;
		}

		message HelloReply {
		  string message = 1;
		}

4. make file based on protobuf with grpc_tool

		mkdir compiled
		python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. demo.proto

--python_out: directory for generated python file for protobuf message
--grpc_python_out: directory for generated python file for server client class relevant

5. program the client.py and server.py



## Java Demo
1. create a maven project
2. define protobuf(make sure always use the same protobuf)
3. download depandency and generate code with maven. Use a version which work with java 1.6!
https://github.com/grpc/grpc-java/tree/v1.14.x, have a look at the pom.xml
4. program the server and client
