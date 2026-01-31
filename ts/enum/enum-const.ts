// 常量枚举示例
const enum Align {
  LEFT,
  CENTER,
  RIGHT,
}

let left = Align.LEFT;
console.log(left);

// 不支持迭代器访问的方式
// for (let key in Align) {
//   console.log(`key=${key}, value=${Align[key]}`);
// }
