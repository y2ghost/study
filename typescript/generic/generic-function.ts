// 泛化函数示例
class Pair<F, S> {
  first: F;
  second: S;

  constructor(first: F, second: S) {
    this.first = first;
    this.second = second;
  }
}

// 泛化函数定义
function getFirstArray<F, S>(pairs: Pair<F, S>[]): F[] {
  let arr: F[] = [];
  for (let i = 0; i < pairs.length; i++) {
    let first: F = pairs[i].first;
    arr.push(first);
  }
  return arr;
}

let numArray: Pair<number, boolean>[] = [
  new Pair(1, true),
  new Pair(2, false),
  new Pair(3, true),
];

console.log(getFirstArray(numArray));
