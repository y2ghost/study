// Symbol类型示例
// Symbol不能转换为字符串，也就说alert或是之类的函数不能直接打印它们
// 因为它们对Symbol隐式转换为字符串
let s = Symbol();
console.log(s.toString());
console.log(typeof s);
console.log(typeof s === "symbol");

// 自定义Symbol描述信息，实际用于调试，描述信息并没有特殊目的
let m = Symbol("my symbol");
console.log(m.toString());

m = Symbol(10);
console.log(m.toString());

m = Symbol(new Date());
console.log(m.toString());

// Symbol独一无二
let s1 = Symbol("my symbol");
let s2 = Symbol("my symbol");
console.log(s1 === s2);

// Symbol用于属性名称
let id = Symbol();
let id2 = Symbol();
let employee = {};
employee[id] = 10;
employee[id2] = 20;
employee["name"] = "yy";
console.log(employee[id]);
console.log(employee);
// 会忽略symbol类型的键
for (let k in employee) {
  console.log(k);
}

// 用作隐藏的属性，原因是Symbol类型的键值会被忽略
let json = JSON.stringify(employee);
console.log(json);

// 定义Symbol属性
let id3 = Symbol();
let employee2 = {
  [id3]: 10,
  name: "yy",
};
console.log(employee2);

// 定义symbol对象作为函数或是方法名称
let compare = Symbol();
class Math {
  constructor() {
    this.desc = "math utility";
  }
  // 定义函数名称
  [compare](x, y) {
    return x === y ? 0 : x < y ? -1 : 1;
  }
}

let math = new Math();
console.log(math);
let doCompare = math[compare];
let result = doCompare(7, 5);
console.log(result);

// 全局共享的Symbol对象示例
// 使用方法Symbol.for(key)
let s3 = Symbol.for("test");
let s4 = Symbol.for("test");
console.log(s3 == s4);

// 获取全局共享的Symbol对象的key值示例
let s5 = Symbol.for("test");
let k = Symbol.keyFor(s5);
console.log(k);
let s6 = Symbol("myDesc"); // 注意不是全局共享对象
let k2 = Symbol.keyFor(s6);
console.log(k2);
