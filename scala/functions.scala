object functions {
  // 函数语法示例
  def run {
    def noArgsFunc {
      println("我是无参数的函数")
    }
    
    // 调用无参数的函数
    noArgsFunc
    
    def sqrt(x: Double): Double = {
      if (x < 0) {
        return -1
      }
      
      var q: Double = x / 2
      for (i <- 1 to 20) {
        q = (q + x / q) / 2
      }
      
      return q
    }
    
    println(sqrt(61))
    
    def printInfo(name: String, age: Int) {
      println("name: " + name + " Age: " + age)
    }
    
    printInfo("李四", 66)
    
    // 可变参数示例
    def min(args: Int*): Int = {
      if (0 == args.length) {
        return 0
      }
      
      var min = args(0)
      for (i <- args) {
        if (i < min) {
          min = i
        }
      }
      
      return min
    }
    
    println("最小值: " + min(13, -30, 2, -17, 37))
  }
}