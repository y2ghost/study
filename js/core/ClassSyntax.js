// 支持class关键字定义OOP风格的语法
// 本质还是JS的原型继承，只不过属于方便的语法糖
class Employee {
  constructor(name, dept) {
    this.name = name;
    this.dept = dept;
  }
}

let e = new Employee("yy", "Admin");
console.log(e);

// getter和setter方法
// 属性使用下划线的方式，目的避免方法和属性同名导致可能的异常递归
class Employee2 {
  get name() {
    return this._name;
  }
  set name(name) {
    this._name = name;
  }
  get dept() {
    return this._dept;
  }
  set dept(value) {
    this._dept = value;
  }
}

e = new Employee2();
console.log(e);
e.name = "gg";
e.dept = "Admin";
console.log(e);
console.log(e.dept);

// 类方法定义
class Number {
  constructor(x) {
    this.x = x;
  }
  square() {
    return this.x * this.x;
  }
}

let number = new Number(4);
console.log(number.square());

// 类静态方法定义
class Util {
  static square(number) {
    return number * number;
  }
}

let result = Util.square(3);
console.log(result);

// 类静态变量成员定义
class Util2 {
  static rectangleArea(x, y) {
    let area = x * y;
    return area.toFixed(Util2.DECIMAL_PLACES);
  }
}

Util2.DECIMAL_PLACES = 2; //唯一的定义方法
console.log(Util2.rectangleArea(5.77, 3.99));

// 类继承示例
class Shape {
  constructor(name) {
    this.name = name;
  }

  draw() {
    console.log("drawing a shape named " + this.name);
  }
}

class Circle extends Shape {
  constructor(name, radius) {
    super(name);
    this.radius = radius;
  }

  draw() {
    super.draw();
    console.log("radius " + this.radius);
  }
}

let circle = new Circle("circle", 3);
circle.draw();
console.log(circle instanceof Shape);

// 类用作表达式
let numberCls = class {
  constructor(value) {
    this.value = value;
  }
  square() {
    return this.value * this.value;
  }
};
result = new numberCls(3).square();
console.log(result);
