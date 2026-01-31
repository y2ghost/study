cmake_minimum_required(VERSION 3.20.0)

if (1 LESS 2)
  message("正确: 1小于2")
endif()

if (20 EQUAL "20 Z 20")
  message("数字和字符串比较一般都是false，但字符串开头为数字属于例外，别依赖这种行为")
endif()

if (1.3.4 VERSION_LESS_EQUAL 1.4)
  message("版本小于等于1.4")
endif()

