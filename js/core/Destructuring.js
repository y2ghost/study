// 解构赋值示例
// 数组解构, 必须使用[...]包裹变量定义
var arr = [1, 3, 5];
var [x, y, z] = arr;
console.log(x);
console.log(y);
console.log(z);

// 对象解构, 必须使用{...}包裹变量定义
var employee = { name: "Yy", role: "Developer", salary: 5000 };
var { name, role, salary } = employee;
console.log(name);
console.log(role);
console.log(salary);

// 对象解构一般都是相同名称的变量得到赋值，也可以自定义名称
var employee2 = { name: "Tt", role: "Developer", salary: 5000 };
var { name: n, role: r, salary: s } = employee2;
console.log(n);
console.log(r);
console.log(s);

// 剩余参数赋值
var arr2 = [1, 3, 5];
var [xx, ...others] = arr;
console.log(xx);
console.log(others);

// 函数参数支持解构
function print([x, y]) {
  console.log(x);
  console.log(y);
}
print([3, 4]);

// 函数参数支持解构并设置默认参数
function print2(x, { name = "N/A", role = "IT", salary = 0 } = {}) {
  console.log(x);
  console.log(name);
  console.log(role);
  console.log(salary);
}
print2(3);
console.log("--");
var employee3 = { name: "YY", role: "Admin", salary: 3000 };
print2(2, employee3);
