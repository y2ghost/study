// 接口混合类型示例
interface Circle {
  radius: number; // 属性
  (x: number, y: number): void; // 函数定义
  display(b: boolean); // 方法
  [state: string]: any; // 索引器
}

// 首先创建函数
let c = function (x: number, y: number) {
  console.log(`center position: (${x}, ${y})`);
};

let circle: Circle = c as Circle; // 类型转换
circle.radius = 10;
circle.display = function (d: boolean) {
  console.log("circle displayed: " + d);
};
circle["interactive"] = true;
circle["maximumRadius"] = 20;

console.log(circle);
circle(2, 5); // 调用索引函数
circle.display(true);
