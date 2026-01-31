// 一个生成器函数(Generator Functions)就是特殊的支持iterator协议的函数
// 定义方式就是在function加上 '*'关键字
function* myGenerator() {}
let myGen = myGenerator();
let s = myGen.next();
console.log(s);

// 使用yield关键字
// 如果调用next()的次数不够多，直到最后一个yield语句执行，生成器方法将永远不会完成
function* myGenerator2() {
  yield 3;
  console.log("生成器函数结束");
}
myGen = myGenerator2();
s = myGen.next();
console.log(s);
// 此处调用yield语句已经完成，从而触发函数的剩余逻辑代码执行
s = myGen.next();
console.log(s);

// 可以使用for-of循环
function* myGenerator3() {
  yield 3;
  yield 5;
  yield 7;
}
myGen = myGenerator3();
for (let v of myGen) {
  console.log(v);
}

// 传递参数例子
function* getEvenNumbers(start, end) {
  for (let i = start; i <= end; i++) {
    if (i % 2 == 0) {
      yield i;
    }
  }
}

let evenNumbers = getEvenNumbers(3, 9);
for (let n of evenNumbers) {
  console.log(n);
}

// 支持使用next函数
evenNumbers = getEvenNumbers(3, 9);
let temp = { done: false };
while (!temp.done) {
  console.log((temp = evenNumbers.next()));
}
