cmake_minimum_required(VERSION 3.20.0)

function(MyFunction FirstArg)
  message("函数: ${CMAKE_CURRENT_FUNCTION}")
  message("文件: ${CMAKE_CURRENT_FUNCTION_LIST_FILE}")
  message("FirstArg: ${FirstArg}")
  set(FirstArg "函数设置的值")
  message("FirstArg: ${FirstArg}")
  message("ARGV0: ${ARGV0} ARGV1: ${ARGV1} ARGC: ${ARGC}")
endfunction()

set(FirstArg "初始化的值")
MyFunction("值1" "值2")
message("FirstArg(全局): ${FirstArg}")

