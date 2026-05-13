cmake_minimum_required(VERSION 3.20.0)

set(MyString1 "变量值都是字符串")
set([[My String2]] "文本2")
set("My String 3" "文本3")
message(${MyString1})
message(${My\ String2})
message(${My\ String\ 3})
