import scala.io.StdIn.{readLine, readInt}

object controlBlocks {
  // 控制语句示例
  def run {
    var x = 100
    var y = 200
    
    if (x < y) {
      println("x > y")
    } else if ( x < y) {
      println("x < y");
    } else {
      println("x == y")
    }
    
    // 循环语法示例，变量作用域区别于Java和C之类的语言
    var i: Int = 0
    for (i <- 1 to 10) {
      println("循环里面i的值: " + i)
    }
    
    println("循环外面i的值: " + i)
    // 支持过滤表达式
    for (i <- 1 to 100 if i % 2 == 0) {
       println("循环里面i的值: " + i)
    }
    
    // while语法示例
    var answer = 0
    var guessNumber = (Math.random * 1000).toInt + 1
    printf("请你猜数字(%d)，输入1-1000内的数字", guessNumber)
    
    while (answer != guessNumber) {
      println("输入数字: ")
      answer = readInt
      if (answer < guessNumber) {
        println("猜小了")
      } else if (answer > guessNumber) {
        println("猜大了")
      }
    }
    
    println("恭喜你猜对了: " + guessNumber)
  }
}