// 接口本本身用于强类型约束，用于编译期间严格约束检查而已
// 一旦编译后的JS代码，运行时没有任何接口信息
// 可以用来进行面向接口的编程
interface Person {
  name: string;
  age: number;
}

// 接口对象属性字段顺序不重要
let p1: Person = { name: "yy", age: 29 };
let p2: Person = {age: 29, name: "tt"};
