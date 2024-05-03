package ywork.script
import java.time.LocalDateTime

// 函数括号可以省略
println "test" // 同 println("test")

void showSum(int a, int b) {
    println a + b
}

showSum 3, 4

void showTimeNow() {
    println LocalDateTime.now()
}


showTimeNow()
// 出现二义性的时候，必须使用括号保证
println Math.pow(3, 4)
