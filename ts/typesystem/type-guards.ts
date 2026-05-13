// 用来演示确保类型正确的示例
// 使用typeof判断基本类型示例
function show(x: number | string): void {
  console.log("typeof x: " + typeof x);
  if (typeof x == "number") {
    console.log("a number: " + x);
  } else {
    console.log("a string: " + x);
  }
}

show("test string");
show(4);

// 对象判断，永远返回object
class Person {}
let person = new Person();
console.log(typeof person);
console.log(typeof new String("test"));
console.log(typeof null);

// 都是对象，但类型不一样，此处演示类型断言方法
class Car {
  drive() {
    console.log("car driving");
  }
}

class Bike {
  ride() {
    console.log("Bike ridding");
  }
}

function move(vehicle: Bike | Car): void {
  if ((vehicle as Car).drive) {
    (vehicle as Car).drive();
  } else {
    (vehicle as Bike).ride();
  }
}

move(new Bike());
move(new Car());

// 可以单独定义类型断言函数
// 类型谓词: parameterName is Type
function isCar(vehicle: Bike | Car): vehicle is Car {
  return (vehicle as Car).drive != undefined;
}

function move2(vehicle: Bike | Car): void {
  if (isCar(vehicle)) {
    vehicle.drive();
  } else {
    vehicle.ride();
  }
}
move2(new Bike());
move2(new Car());

// 自定义基本类型的断言函数
function isNumber(x: any): x is number {
  return typeof x == "number";
}

function show2(x: number | string): void {
  if (isNumber(x)) {
    console.log("number value: " + x);
  } else {
    console.log("string value: " + x);
  }
}

show2("hello");
show2(4);

// 使用instanceof判断类型
function move3(vehicle: Bike | Car): void {
  if (vehicle instanceof Car) {
    vehicle.drive();
  } else {
    vehicle.ride();
  }
}
move3(new Bike());
move3(new Car());

// 特殊类型的判断: null, undefined
// 默认情况下，下面的函数参数类型就是一样的判断处理
// 函数参数类型:(T | null | undefined ), (T | null), (T)
// 除非使用--strictNullChecks的编译选项
function show3(x: number | null | undefined) {
  if (x === undefined) {
    console.log("value not set");
  } else if (x === null) {
    console.log("value is null");
  } else {
    console.log(x);
  }
}

let x = 10;
let y;
let z = null;
show3(x);
show3(y);
show3(z);

// 可选类型的判断示例
// 默认情况下，下面的函数参数类型就是一样的判断处理
// 函数参数类型:(T | undefined ), (T)
function show4(x?: number) {
  if (x === undefined) {
    console.log("value not set");
  } else if (x === null) {
    console.log("value is null");
  } else {
    console.log(x);
  }
}

let x4 = 10;
let y4;
let z4 = null;
show4(x4);
show4(y4);
show4(z4);

// 使用'!'操作符断言对象不为null值
function splitInHalf(str: string | null) {
  let checkString = function () {
    if (str == null || str == undefined) {
      str = "test";
    }
  };
  checkString();
  return str!.substring(0, str!.length / 2);
}

let s = splitInHalf("bean");
console.log(s);
