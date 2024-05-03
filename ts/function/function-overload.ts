// 函数重载例子
function sum(x: number, y: number): number;
function sum(x: number, y: number, z: number): number;
// 综合实现上面的重载函数
function sum(x: number, y: number, z?: number): number {
  if (typeof z == "undefined") {
    return x + y;
  } else {
    return x + y + z;
  }
}

let n = sum(1, 2);
console.log(n);
n = sum(1, 2, 3);
console.log(n);

// 类方法重载示例
class Util {
  static divide(x: number, y: number): number;
  static divide(str: string, y: number): string[];
  static divide(x: any, y: number): any {
    if (typeof x == "number") {
      return x / y;
    } else if (typeof x == "string") {
      return [x.substring(0, y), x.substring(y)];
    }
  }
}

n = Util.divide(6, 2);
console.log(n);
let s: string[] = Util.divide("football", 4);
console.log(s);
