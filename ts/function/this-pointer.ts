// 如果下面的注释代码(getAreaFunction函数部分)进行如下编译:
// tsc --noImplicitThis this-pointer.ts 编译会出错
// 原因是getAreaFunction调用后，里面的函数this指向的是全局对象
// 严格模式下，this的值: undefined
// 解决方案，使用箭头函数
class Rectangle {
  private w: number;
  private h: number;

  constructor(w: number, h: number) {
    this.w = w;
    this.h = h;
  }

  // getAreaFunction() {
  //   return function (): number {
  //     return this.w * this.h;
  //   };
  // }
  getAreaFunction() {
    return () => {
      return this.w * this.h;
    };
  }
}

let rectangle = new Rectangle(2, 5);
let areaFunction = rectangle.getAreaFunction();
let area = areaFunction();
console.log(area);

// 显示指定this值
class Rectangle2 {
  private w;
  private h;

  constructor(w, h) {
    this.w = w;
    this.h = h;
  }

  getAreaFunction(this: Rectangle2) {
    return () => {
      return this.w * this.h;
    };
  }
}

// 阻止使用this值，也就是编译的时候会报错
// class Rectangle3 {
//   private w;
//   private h;
//
//   constructor(w, h) {
//     this.w = w;
//     this.h = h;
//   }
//
//   getAreaFunction(this: void) {
//     return () => {
//       return this.w * this.h;
//     };
//   }
// }
