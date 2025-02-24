// 箭头函数示例
var run = () => console.log("hi yy");
run();

var square = (x) => x * x;
console.log(square(5));

var sum = (a, b) => a + b;
console.log(sum(3, 8));

// 返回一个对象
var rectangle = (w, h) => ({ width: w, height: h, area: w * h });
console.log(rectangle(3, 4));

// 作为函数参数
var nums = [2, 4, 7, 9];
var evens = nums.filter((n) => n % 2 == 0);
console.log(evens);

// ES6之前，嵌套函数绑定全局的this对象
function Rectangle() {
  this.w = 10;
  this.h = 5;
  this.area = 0;
  // 传入的嵌套函数绑定的并非Rectangle的this值
  setTimeout(function () {
    console.log(this.w);
    console.log(this.h);
    this.area = this.w * this.h;
    console.log(this.area);
  }, 1);
}
new Rectangle();

// 使用箭头函数，虽然没有自己的this值，但绑定的this值是封闭它的执行对象
// 记住setTimeout函数设置的回调函数会覆盖上面的例子，所以上面的时间间隔
// 设置很短，以确保不和下面的例子冲突
function Rectangle2() {
  this.w = 10;
  this.h = 5;
  this.area = 0;
  setTimeout(() => {
    this.area = this.w * this.h;
    console.log(this.area);
  }, 100);
}
new Rectangle2();
