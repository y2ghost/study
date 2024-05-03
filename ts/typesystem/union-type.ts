// union type允许使用多种类型的选择，限定类型范围
// any允许选择使用所有类型

function square(x: number | string): number {
  if (typeof x == "string") {
    let a = parseInt(x);
    if (isNaN(a)) {
      throw Error("x is not a number: " + x);
    }

    x = a;
  }

  return Math.pow(x, 2);
}

let result = square("4");
console.log(result);
result = square(5);
console.log(result);
