// 只读属性示例
interface Person {
  readonly name: string;
  readonly age: number;
}

let p: Person = { name: "yy", age: 29 };
console.log(p);
// 不能设置只读属性值
// p.age = 30
