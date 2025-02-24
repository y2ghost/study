object expressions {
  // 表达式语法示例
  def run {
    println(78 * 9)
    println(41.3 + 99.7)
    println(77.1 * (4.2f - 6.6))
    println(88 < 77 || ((22 & 1) == 0))
    println(("good" + " " + "life") * 3)
    println("match 2 case: " + matchFruit(2))
    println("match 4 case: " + matchFruit(4))
    println("match 5 case: " + matchFruit(5))
    testClosure
  }
  
  def matchFruit(index: Int): String = index match {
    case 1 => "apple"
    case 2 => "banana"
    case 3 => "kumquat"
    case 4|5|6 => "orange"
    case _ => "unknown"
  }
  
  def testClosure {
    var divisor = 9
    var divideClosure = (i: Int) => i / divisor
    println("90/9 = " + divideClosure(90))
    
    def execureClosure(closure: (Int) => Int, parameter: Int) {
      println("closure调用结果: " + closure(parameter))
    }
    
    execureClosure(divideClosure, 90)
  }
}