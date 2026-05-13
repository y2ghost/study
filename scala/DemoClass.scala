// 类的用法示例
class DemoClass(myName: String, myAge: Int, myCity: String) {
  var name: String = myName
  var age: Int = myAge
  var city: String = myCity
  
  def this() {
    this("unknown", 0, "unknown")
  }
  
  def this(myAge: Int) {
    this("unknown", myAge, "unknown")
  }
  
  def printInfo() {
    println("name: " + name)
    println("age: " + age)
    println("city: " + city)
  }
}