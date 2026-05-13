// for-of 的用法示例，适合实现了iterable的对象
let fruits = ["apple", "banana", "orange"];
for (let fruit of fruits) {
  console.log(fruit);
}

for (let [index, value] of fruits.entries()) {
  console.log(index + " - " + value);
}

for (let index of fruits.keys()) {
  console.log(index);
}

for (let value of fruits.values()) {
  console.log(value);
}

// for-in的用法示例，可以用于任何对象
let obj = { x: 2, y: "test" };
for (let k in obj) {
  console.log(`${k} = ${obj[k]}`);
}

// 自定义迭代器，也就是实现如下的函数
// 第一个是否可迭代(iterable): 函数名称: Symbol.iterator
// 第二个就是next函数实现，返回一个对象包含done和value键
class EvenNumbers {
  constructor(start, end) {
    this._start = 2 * Math.round(start / 2) - 2;
    this._end = end;
  }

  // 实现iterable协议
  [Symbol.iterator]() {
    let [current, end] = [this._start, this._end];
    return {
      // 实现next函数
      next() {
        if (current <= end) {
          current += 2;
        }
        if (current > end) {
          return { done: true };
        } else {
          return { value: current, done: false };
        }
      },
    };
  }
}

let evenNums = new EvenNumbers(5, 11);
for (let n of evenNums) {
  console.log(n);
}

console.log("-----------");
console.log("使用对象的next函数例子:");
console.log("-----------");
let evenIterator = evenNums[Symbol.iterator]();
while (true) {
  let obj = evenIterator.next();
  console.log(obj);
  if (obj.done) {
    break;
  }
}
