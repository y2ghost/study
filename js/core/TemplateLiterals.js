// 多行字符串例子
var str = `A logic     a day 
helps you  
work and play.`;
console.log(str);

// 模板字符串例子
var a = 5;
var b = 6;
var result = `Addition:  ${a} + ${b} = ${a + b} `;
console.log(result);

function compare(a, b) {
  var result = `${a} is ${a > b ? "greater" : "less"} than ${b}`;
  return result;
}
console.log(compare(3, 5));
console.log(compare(7, 5));

// 标签模板字符串例子(Tagged Templates)
function printAll(literalArray, operator1, operator2, result) {
  console.log(literalArray);
  console.log(operator1);
  console.log(operator2);
  console.log(result);
}
a = 3;
b = 4;
printAll`Addition:  ${a} + ${b} = ${a + b}`;

// 标签函数内部使用模板字符串
function getSimplified(literalArray, operator1, operator2, result) {
  console.log(`literalArray= ${literalArray}`);
  literalArray.forEach((v, i) => console.log(`literalArray[${i}]=${v}`));
  console.log(`operator1= ${operator1}`);
  console.log(`operator2= ${operator2}`);
  console.log(`result= ${result}`);
  return `${literalArray[0].trim()} ${result}`;
}
a = 3;
b = 4;
var result = getSimplified`Addition:  ${a} + ${b} = ${a + b}`;
console.log(result);

// 原始字符串，不会处理转义，但会处理${}模板占位符
var a = 3;
var b = 4;
var myString = String.raw`sum:\n ${a + b}`;
console.log(myString);

// 标签函数内部获取原始字符串
function indexTest(literalArray) {
  console.log(literalArray.raw[0]);
  console.log(literalArray[0]);
}
indexTest`"finding \n errors"`;
