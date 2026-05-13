cmake_minimum_required(VERSION 3.20.0)
project(Scope)

# 变量作用域示例
function(Inner)
  message("  > 内部: ${V}")
  set(V 3)
  message("  < 内部: ${V}")
endfunction()

function(Outer)
  message(" > 外部: ${V}")
  set(V 2)
  Inner()
  message(" < 外部: ${V}")
endfunction()

set(V 1)
message("> 全局: ${V}")
Outer()
message("< 全局: ${V}")

