// 使用extends约束类型示例

interface Shape {
  draw();
}

// 约束类型为Shape或其子类
function drawShapes<S extends Shape>(shapes: S[]): void {
  shapes.forEach((shape) => shape.draw());
}

class Circle implements Shape {
  draw() {
    console.log(`drawing Circle`);
  }
}

class Rectangle implements Shape {
  draw() {
    console.log(`drawing Rectangle`);
  }
}

let circle = new Circle();
let rectangle = new Rectangle();
drawShapes([circle, rectangle]);

// 可以使用keyof推断类型
function getProp<T, K extends keyof T>(key: K, obj: T): any {
  return obj[key];
}

let obj = { a: 2, b: 3, c: 4 };
let prop = getProp("c", obj);
console.log(prop);

// 构造函数类型示例
function createInstance<T>(
  t: new (...constructorArgs: any[]) => T,
  ...args: any[]
): T {
  return new t(args);
}

class Test {
  private x: number;

  constructor(x: number) {
    this.x = x;
  }
}

let test: Test = createInstance(Test, 5);
console.log(test);

// 构造函数类型示例2
function createInstance2<R, T extends { new (...constructorArgs: any[]): R }>(
  constructor: T,
  ...args: any[]
): R {
  return new constructor(args);
}

class Test2 {
  private x: number;

  constructor(x: number) {
    this.x = x;
  }
}

let test2: Test2 = createInstance2(Test2, 6);
console.log(test2);
