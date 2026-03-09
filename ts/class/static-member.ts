// 类的静态变量和成员示例
class Counter {
  static count: number = 0;

  static updateCounter(): number {
    return ++Counter.count;
  }
}

let count = Counter.updateCounter();
console.log(count);
