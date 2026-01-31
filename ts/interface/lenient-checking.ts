// 如果一个对象没有定义类型，那么只要有接口所有的属性，都可以赋值
interface Person {
  name: string;
  age: number;
}

function displayPerson(person: Person): void {
  console.log(person);
}

let p = { name: "yy", age: 29, phone: "111-111-111" };
// p的属性完全兼容Person接口，且没有定义类型，所以可以赋值
let p2: Person = p;
displayPerson(p);
// 但是对象字面量则不行，下面的注视代码通不过，原因本质还是类型不匹配
// 它属于特定的字面量类型
// displayPerson({name: "TT", age: 29, phone: "111-111-111"});
// 下面的类型转换则可以
displayPerson({ name: "TT", age: 29, phone: "111-111-111" } as Person);
