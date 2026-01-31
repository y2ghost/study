// 接口可选属性
interface Person {
  name: string;
  age: number;
  phone?: string;
}

let p: Person = { name: "TT", age: 29 };
console.log(p);
