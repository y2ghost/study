// showXXX函数的模块文件

export default function showFunctionNames() {
  console.log("use functions: showSquare, showSum and ShowSquareRoot");
}

export function showSquare(num) {
  show(num * num);
}

export function showSum(x, y) {
  show(x + y);
}

export function showSquareRoot(x) {
  show(Math.sqrt(x));
}

function show(result) {
  console.log(result);
}
