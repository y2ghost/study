object MainObject {
  def main(args: Array[String]): Unit = {
    vars.run
    expressions.run
    functions.run
    controlBlocks.run
    collections.run
    
    var someClass: DemoClass = new DemoClass
    someClass.name = "张三"
    someClass.printInfo
    
    var someClass2: DemoClass2 = new DemoClass2
    someClass2.name = "李四"
    someClass2.printInfo
  }
}