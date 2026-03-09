// 类型别名示例
// 语法: type myTypeName = <a valid TypeScript type>
// 简单示例
type chars = string;
function show(param: chars): void {
  console.log(param);
}
show("hello");

// union类型别名示例
type num = number | string;
function square(n: num): number {
  if (typeof n === "string") {
    n = parseInt(n);
  }
  return Math.pow(n, 2);
}

console.log(square(3));
console.log(square("5"));

// 对象属性示例
type Product = { name: string; price: number };
let p: Product = { price: 100, name: "Monitor" };
console.log(p);

// 函数示例
type StringRemover = (input: string, index: number) => string;
let remover: StringRemover = function (str: string, i: number): string {
  return str.substring(i);
};
let s = remover("Hi there", 3);
console.log(s);

// 数组类型示例
type State = [string, boolean];
let a: State = ["active", true];
console.log(a);

// 字面量类型示例
type Alignment = "Left" | "RIGHT" | "CENTER";
function doAlign(alignment: Alignment): void {
  console.log(alignment);
}
doAlign("Left");

type WIDTH = 100 | 200 | 300;
function setWidth(w: WIDTH) {
  console.log(w);
}
setWidth(100);
