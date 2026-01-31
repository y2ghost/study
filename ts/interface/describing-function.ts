// 定义接口方法示例
// 用来重用代码，约束方法的签名
interface StringRemover {
  (input: string, index: number): string;
}

let remover: StringRemover = function (str: string, i: number): string {
  return str.substring(i);
};

let s = remover("Hi there", 3);
console.log(s);
