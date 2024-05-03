// 字符串枚举值示例
enum NumSymbol {
  K = "kilo",
  M = "mega",
  G = "giga",
}

console.log(NumSymbol.K);
console.log(NumSymbol.M);
console.log(NumSymbol.G);

console.log("-- looping --");
for (let key in NumSymbol) {
  console.log(`key=${key}, value=${NumSymbol[key]}`);
}
