// 整数枚举，默认从0开始
enum Align {
  LEFT,
  CENTER,
  RIGHT,
}

console.log(Align);
let a: Align = Align.LEFT;
console.log(a);
let a2: String = Align[0];
console.log(a2);

console.log("-- looping --");
for (let alignKey in Align) {
  console.log(`key=${alignKey}, value=${Align[alignKey]}`);
}

// 指定值，后面的依次递增
enum Align2 {
  LEFT,
  CENTER = 9,
  RIGHT,
}

console.log(Align2.LEFT);
console.log(Align2.CENTER);
console.log(Align2.RIGHT);
console.log("-- looping --");
for (let key in Align2) {
  console.log(`key=${key}, value=${Align2[key]}`);
}

// 获取所有的枚举值
enum Align3 {
  LEFT,
  CENTER,
  RIGHT,
}

// 获取整数值列表
let values: Align[] = Object.keys(Align)
  .map((key) => Align[key])
  .filter((value) => typeof value === "number");
console.log(values);

// 获取字符串值列表
let sValues: Align[] = Object.keys(Align)
  .map((key) => Align[key])
  .filter((value) => typeof value === "string");
console.log(sValues);
