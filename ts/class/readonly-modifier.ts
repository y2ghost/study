// 用const关键字定义的变量必须在声明时初始化
// 它可以与任何变量一起使用，但不能作为类成员使用
// 用readonly修饰符定义的变量只用于属性，它的初始化可以延迟到构造函数中
// 这个修饰符阻止我们重新分配属性值

class Person {
  private readonly _name: string;
  private readonly _age: number;

  constructor(name: string, age: number) {
    this._name = name;
    this._age = age;
  }

  get name(): string {
    return this._name;
  }

  get age(): number {
    return this._age;
  }

  displayAsString(): void {
    // 错误代码: this._age=30;  因为_age只读
    console.log(this);
  }
}

let person: Person = new Person("yy", 28);
person.displayAsString();
