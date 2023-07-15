// 类继承语法示例
// class Person{}
// class Employee extends Person {}

// 构造函数调用例子
class Person {
  _name: string;
  _age: number;

  constructor(name: string, age: number) {
    this._name = name;
    this._age = age;
  }

  displayAsString(): void {
    console.log(this);
  }
}

class Employee extends Person {
  _salary: number;

  constructor(name: string, age: number, salary: number) {
    // 必须调用父类的构造函数
    super(name, age);
    this._salary = salary;
  }
  // 方法覆盖
  displayAsString(): void {
    console.log("Displaying employee:");
    super.displayAsString();
  }
}

let emp = new Employee("yy", 23, 3000);
console.log(emp);
emp.displayAsString();

// 子类不定义构造函数，默认继承父亲类
class Component {
  width: number;
  height: number;

  constructor(width: number, height: number) {
    this.width = width;
    this.height = height;
  }
}

class Button extends Component {
  onClick() {
    console.log("button clicked");
  }
}

let button: Button = new Button(10, 5);
console.log(button);
button.onClick();

// 抽象类的示例
abstract class Shape {
  private _name: string;

  constructor(name: string) {
    this._name = name;
  }

  public draw(): void {
    console.log("pre drawing " + this._name);
    this.drawShape();
  }

  // 抽象方法
  protected abstract drawShape();
}

class Square extends Shape {
  private _length: number;

  constructor(name: string, length: number) {
    super(name);
    this._length = length;
  }

  // 必须实现抽象方法
  protected drawShape() {
    console.log("drawing square with length " + this._length);
  }
}

let shape: Shape = new Square("saure", 5);
shape.draw();
