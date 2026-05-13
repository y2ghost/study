// 将函数参数当作一个数组类型对待，剩余参数操作符...
// 只能用在参数的后面，不能使用在前面
function print(...args) {
  console.log(args.length);
  console.log(args);
  for (let i = 0; i < args.length; i++) {
    console.log(args[i]);
  }
}
print(5, "test", new Date());

function print2(num, ...args) {
  console.log(num);
  console.log(args);
  for (let i = 0; i < args.length; i++) {
    console.log(args[i]);
  }
}
print2(5, "test", new Date());

// 错误用法如下
// function print3(...args, num) {
//     console.log(num);
//     console.log(args);
//     for (let i = 0; i < args.length; i++) {
//         console.log(args[i]);
//     }
// }
// print3(5, 'test', new Date());
