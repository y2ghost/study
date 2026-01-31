cmake_minimum_required(VERSION 3.20.0)

macro(Test MyVar)
  set(myVar "宏设置新值")
  message("argument: ${myVar}")
endmacro()

set(myVar "初始化的值")
message("myVar变量值: ${myVar}")
Test("called value")
message("myVar变量值: ${myVar}")

