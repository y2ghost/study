package ywork.script
// import语句说明
// 默认导入如下的类
// import java.lang.*
// import java.util.*
// import java.io.*
// import java.net.*
// import groovy.lang.*
// import groovy.util.*
// import java.math.BigInteger
// import java.math.BigDecimal

// 不需要导入
def list = List.of(1, 3,)
def date = new Date();

// 需要导入
import java.time.LocalDateTime
def now = LocalDateTime.now()

// 静态导入和别名
import java.time.LocalTime as Time
import static java.lang.Math.pow as power

println Time.now()
println power(3,5)
