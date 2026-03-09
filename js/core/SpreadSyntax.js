// JS展开语法操作符...，实际和剩余参数操作符一样
// 用于不同的地方，处理方式不同
function test(a, b, c) {
  console.log(a);
  console.log(b);
  console.log(c);
}
var arr = [1, 2, 3];
test(arr);
test(...arr);
console.log("-------");

// 混合参数使用例子
function test2(a, b, c, d) {
  console.log(a);
  console.log(b);
  console.log(c);
  console.log(d);
}
var arr2 = [1, 2, 3];
test2(...arr2, 10);
console.log("-------");
test2(10, ...arr2);

console.log("-------");
// 展开操作同Function.prototype.apply()的功能类似
function test3(a, b, c) {
  console.log(a);
  console.log(b);
  console.log(c);
}
var arr3 = [1, 2, 3, 4, 5];
test3.apply(null, arr3);
