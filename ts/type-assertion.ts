// 使用尖括号类型转换
let x: any = "hi there";
let s = (<string>x).substring(0, 3);
console.log(s);

// 使用as关键字
s = (x as string).substring(0, 3);
console.log(s);
