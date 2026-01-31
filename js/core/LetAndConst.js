// let 和 const关键字用法示例
// let 主要是函数内部，代码快内部使用，使用它定义的变量
// 必须从定义开始直到文件结束
// const同let的作用，但不允许修改变量值
// var定义变量不受范围限制，容易重复定义，建议使用let和const

// 变量作用于代码块内部
{
  let x = 3;
}
// 注释的代码会报错，找不到变量定义
// console.log(x);

// 此处var定义的变量不受到代码块影响
{
  var x = 3;
}
console.log(x);

const y = 5;
console.log(y);
// 不能对y重新赋值，下面注释代码会报错
// y = 6;
