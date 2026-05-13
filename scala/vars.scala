object vars {
  // 变量语法示例
  def run {
    println("创建变量示例")
    var myVariable = 0
    val myValue = 0
    myVariable = 10
    // val不能被改变
    // myValue = 10
    var myInt: Int = 0
    var personName: String = "张三"
    var goldenRaio: Double = 1.61803398875
    // 标识符不限制
    val `#*……`: Int = 666
    var someChar: Char = 'A'
    var someBool: Boolean = false
    
    // 类型转换
    var someDouble = 1.3
    println("转为float: " + someDouble.toFloat)
    println("转为char: " + someDouble.toChar)
    println("转为int: " + someDouble.toInt)
    println("转为string: " + someDouble.toString)
  }
}