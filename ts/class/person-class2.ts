// 演示typescript类的语法示例
// 编译: tsc --target es6 person-class2.ts
class Person2 {
  _name: string;
  _age: number;

  constructor(name: string, age: number) {
    this._name = name;
    this._age = age;
  }

  displayAsString(): void {
    console.log(this);
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get age(): number {
    return this._age;
  }

  set age(value: number) {
    this._age = value;
  }
}

let person: Person2 = new Person2("yy", 23);
person.displayAsString();
let pName: string = person.name;
console.log(pName);
person.age = 25;
person.displayAsString();
