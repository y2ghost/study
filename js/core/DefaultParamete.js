// 函数默认参数示例
function getArea(w, h = 1) {
  return w * h;
}
console.log(getArea(3));
console.log(getArea(3, 2));

// 数组作为默认参数
function print(x = [1, 2, 3]) {
  console.log(x);
}
print();
print(5);
print([5, 6]);

// 默认参数引用
function getArea(w, h = w) {
  return w * h;
}
console.log(getArea(5));

// 使用函数返回值作为默认参数
function getArea(w, h = getH()) {
  return w * h;
}

function getH() {
  return 3;
}
console.log(getArea(5));
