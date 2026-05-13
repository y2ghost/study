// 参数属性属于语法糖，就是在构造函数参数位置直接进行
// 属性定义和值初始化
class Person {
  constructor(private _name: string, private _age: number) {}

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

let person: Person = new Person("TT", 29);
console.log(person);
