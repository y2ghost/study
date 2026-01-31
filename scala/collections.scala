object collections {
  def run {
    // 数组示例
    var myIntArray: Array[Int] = new Array[Int](5)
    var myDoubleArray = new Array[Double](10)
    val myStringArray = new Array[String](3)

    myDoubleArray(0) = 99.0
    myDoubleArray(1) = 25.5 / 100.0
    myDoubleArray(2) = Math.sqrt(10)
    myDoubleArray((7 >> 2) + 2) = 4.0
    myDoubleArray(4) = 3.14
    for (i <- 0 to 5) {
      printf("myDoubleArray(%d) = %f%n", i, myDoubleArray(i))
    }

    // 列表示例
    var intList: List[Int] = List(100, 104, 108)
    for (i <- intList) {
      printf("intlist element: %d%n", i)
    }

    // 字典示例
    var m = Map(1 -> "ONE", 2 -> "TWO", 3 -> "THREE")
    println("键集合: " + m.keys)
    println("值集合: " + m.values)
    println("是为空: " + m.isEmpty)
    println("map(1): " + m(1))
  }
}