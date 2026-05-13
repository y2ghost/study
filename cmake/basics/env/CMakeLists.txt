cmake_minimum_required(VERSION 3.20.0)
project(Environment)

message("使用环境变量 " $ENV{myenv})
add_custom_target(EchoEnv ALL COMMAND echo "myenv构建的值 " $ENV{myenv})
