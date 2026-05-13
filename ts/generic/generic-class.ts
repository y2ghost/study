// 泛化类示例
class List<T> {
  private data: T[];

  constructor(...elements: T[]) {
    this.data = elements;
  }

  add(t: T) {
    this.data.push(t);
  }

  remove(t: T) {
    let index = this.data.indexOf(t);
    if (index > -1) {
      this.data.splice(index, 1);
    }
  }

  asArray(): T[] {
    return this.data;
  }
}

let numbers = new List<number>(1, 3, 6, 7);
numbers.add(11);
numbers.remove(3);
let numArray = numbers.asArray();
console.log(numArray);

let fruits = new List<string>("apple", "banana", "orange");
fruits.add("mango");
fruits.remove("apple");
let fruitArray = fruits.asArray();
console.log(fruitArray);

// 多个泛化类型示例
class Pair<F, S> {
  private _first: F;
  private _second: S;

  constructor(first: F, second: S) {
    this._first = first;
    this._second = second;
  }

  get first(): F {
    return this._first;
  }

  get second(): S {
    return this._second;
  }
}

let pair = new Pair<number, boolean>(10, true);
console.log(pair.first);
console.log(pair.second);
console.log(pair);

let pair2 = new Pair<string, number>("1K", 1000);
console.log(pair2.first);
console.log(pair2.second);
console.log(pair2);
