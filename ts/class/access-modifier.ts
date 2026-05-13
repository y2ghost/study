// 访问权限public, protected, private，默认public
// 访问权限目的是语法层面进行分析校验，实际转换成了JS代码，则控制不了
// 类似Java, C++面向对象的定义

// private权限
class Person {
  private _name: string;
  private _age: number;

  constructor(name: string, age: number) {
    this._name = name;
    this._age = age;
    this.init();
  }

  private init() {
    console.log(`person created ${JSON.stringify(this)}`);
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

let person: Person = new Person("YY", 30);
// 不能直接访问变量，使用getter方法
// 错误示范: console.log(person._name);
console.log(person.name);

// 私有构造函数例子
class AppContext {
  private static _instance: AppContext = new AppContext();

  private AppContext() {}

  public static getInstance(): AppContext {
    return AppContext._instance;
  }

  getData(): any {
    return "私有构造函数例子";
  }
}

let data = AppContext.getInstance().getData();
console.log(data);

// protected例子
class Person2 {
  protected _name: string;
  protected _age: number;

  protected constructor(name: string, age: number) {
    this._name = name;
    this._age = age;
  }

  protected displayAsString(): void {
    console.log(this);
  }
}

class Employee extends Person2 {
  private _salary: number;

  constructor(name: string, age: number, salary: number) {
    super(name, age);
    this._salary = salary;
  }

  public display(): void {
    super.displayAsString();
  }
}

//无法直接构造Person2对象
// 错误代码示例:
// let person2: Person2 = new Person2("yy", 23);
let emp: Employee = new Employee("tt", 23, 3000);
// 不能直接访问保护的方法，只能它自身或是子类
// 错误示例: emp.displayAsString();
emp.display();
