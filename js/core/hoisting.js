/**
 * 变量提升说明
 * 建议使用现代化的let和const定义变量
 * 变量提升用于提前使用变量而无需提前定义
 * 类似C++的变量声明，用于递归使用、抽象场景
 * 注意它也会带来问题，谨慎使用
 */

// 打印未定义
console.log(me);
// console.log(job);
// console.log(year);

var me = "张三";
let job = "搬码工";
const year = 2010;

console.log(sum1(2,3));
// 此处也是未初始化，或是未定义
// console.log(sum2(2,3));
// console.log(sum3(2,3));

function sum1(a,b) {
    return a+b;
}

const sum2 = function(a,b) {
    return a+b;
}

const sum3 = (a, b) => a + b;

