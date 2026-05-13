// 演示typescript类的语法示例
class Person {
  // 类成员变量
  name: string;
  age: number;

  // 构造函数
  constructor(name: string, age: number) {
    this.name = name;
    this.age = age;
  }

  // 类方法
  displayAsString(): void {
    console.log(this);
  }
}

let person: Person = new Person("yy", 23);
person.displayAsString();
let pName: string = person.name;
console.log(pName);
