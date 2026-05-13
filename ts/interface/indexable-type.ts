// indexable-type就是根据索引得到值
// 接口支持两种类型的索引键值类型: string 和 number

interface StatesByString {
  [state: string]: boolean;
}

let s1: StatesByString = { enabled: true, maximized: false };
console.log(s1);
console.log(s1["maximized"]);

interface StatesByNumber {
  [index: number]: boolean;
}

let s2: StatesByNumber = [true, false];
console.log(s2);
console.log(s2[0]);

// 使用number类型的索引时，可以添加数组的属性和方法
interface StatesWithMembers {
  [index: number]: boolean;
  length: number;
  pop(): boolean;
}

let s3: StatesWithMembers = [true, false, true];
console.log(s3);
console.log(s3.length);
console.log(s3.pop());
console.log(s3);
