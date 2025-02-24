// JS里面所有函数的参数都是可选的
// 但是在TypeScript里面必须要有
function test(x: number, y: number): void {
  console.log("x: " + x);
  console.log("y: " + y);
}

// 下面的注释代码参数存在问题，编译不通过
// test();
// test(1);
test(1, 2);

// 可选参数示例，可选参数必须必选参数之后
function test2(x: number, y?: number): void {
  console.log("x: " + x);
  console.log("y: " + y);
}

test2(1);
test2(1, 2);

// 默认参数示例
function test3(x: number, y: number = 3): void {
  console.log(`x= ${x}, y=${y}`);
}

test3(2);
test3(2, 5);

// 默认参数不允许出现在函数类型定义中
// let f: (x: number, y: number = 2) => void;

// 剩余参数(rest parameters)示例
function test4(...args: number[]) {
  console.log(args.length);
  console.log(args);
  for (let i = 0; i < args.length; i++) {
    console.log(args[i]);
  }
}
test4(1, 3);

// 剩余参数必须在其他参数后边
function test5(msg: string, ...args: number[]) {
  console.log(args.length);
  console.log(args);
  for (let i = 0; i < args.length; i++) {
    console.log(args[i]);
  }
  console.log(msg);
}
test5("test msg", 1, 3);
