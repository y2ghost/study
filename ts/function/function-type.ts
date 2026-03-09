// 基本定义
function getMessage(count: number): string {
  return `Message no ${count}`;
}

// 接口复用方式
interface MsgFunc {
  (count: number): string;
}

let f1: MsgFunc = function (count: number): string {
  return `Message no ${count}`;
};
let s1: string = f1(1);
console.log(s1);

// 直接指定类型
let f2: (ct: number) => string = function (count: number): string {
  return `Message no ${count}`;
};
let s2: string = f2(1);
console.log(s2);

// 函数作为参数
function process(f: (ct: number) => string) {
  let s: string = f(1);
  console.log(s);
}

process(function (count: number): string {
  return `Message no ${count}`;
});
