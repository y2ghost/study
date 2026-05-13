// TypeScript允许我们遍历某种类型的属性并通过keyof操作符提取其属性的名称
// 该操作符生成对象允许的属性名的类型，可以当作是string的子类型
class A {
  x: number = 5;
}

let y: keyof A = "x";
console.log(y);

// 类函数参数示例
class Test {
  x: number = 6;
}

function getProp(a: keyof Test, test: Test): any {
  return test[a];
}

let t: Test = new Test();
let prop = getProp("x", t);
console.log(prop);
