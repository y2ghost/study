// 泛化接口示例
interface Pair<F, S> {
  first: F;
  second: S;
}

let p: Pair<String, number> = { first: "10K", second: 1000 };
console.log(p);

// 泛化接口带函数
interface Command<T, R> {
  id: T;
  run(): R;
}

let c: Command<String, number> = {
  id: Math.random().toString(36),
  run: function () {
    return 3;
  },
};

console.log(c.id);
console.log(c.run());

// 泛化函数
interface ElementChecker {
  <T>(items: T[], toBeChecked: T, atIndex: number): boolean;
}

function checkElementAt<T>(
  elements: T[],
  toBeChecked: T,
  atIndex: number
): boolean {
  return elements[atIndex] == toBeChecked;
}

let checker: ElementChecker = checkElementAt;
let items = [1, 3, 5, 7];
let b: boolean = checker<number>(items, 5, 1);
console.log(b);
let b2: boolean = checker<number>(items, 5, 2);
console.log(b2);

// 泛化接口索引器
interface StatesOne<R> {
  [state: string]: R;
}

let s1: StatesOne<boolean> = { enabled: true, maximized: false };
console.log(s1);
console.log(s1["maximized"]);

interface StatesTwo<F, S> {
  [state: string]: Pair<F, S>;
}

let s2: StatesTwo<number, boolean> = {
  enabled: { first: 1, second: true },
  maximized: { first: 2, second: false },
};
console.log(s2);
console.log(s2["maximized"]);

// OOP实现泛化接口示例
interface Collection<T> {
  add(t: T): void;
  remove(t: T): void;
  asArray(): T[];
}

class List<T> implements Collection<T> {
  private data: T[] = [];

  add(t: T): void {
    this.data.push(t);
  }

  remove(t: T): void {
    let index = this.data.indexOf(t);
    if (index > -1) {
      this.data.splice(index, 1);
    }
  }

  asArray(): T[] {
    return this.data;
  }
}

class BoolList extends List<boolean> {}

let numbers2: Collection<number> = new List();
numbers2.add(11);
numbers2.add(12);
numbers2.add(13);
numbers2.remove(12);
let numArray2 = numbers2.asArray();
console.log(numArray2);

let boolList: BoolList = new BoolList();
boolList.add(true);
boolList.add(false);
console.log(boolList.asArray());

// 继承泛化接口示例
interface Collection3<T> {
  add(t: T): void;
  remove(t: T): void;
  asArray(): T[];
}

interface List3<T> extends Collection3<T> {
  getElementAt(index: number): T;
}

class ArrayList3<T> implements List3<T> {
  private data: T[] = [];

  add(t: T): void {
    this.data.push(t);
  }

  remove(t: T): void {
    let index = this.data.indexOf(t);
    if (index > -1) {
      this.data.splice(index, 1);
    }
  }

  asArray(): T[] {
    return this.data;
  }

  getElementAt(index: number): T {
    return this.data[index];
  }
}

let numbers3: List3<Number> = new ArrayList3();
numbers3.add(11);
numbers3.add(13);
let numArray3 = numbers3.asArray();
console.log(numArray3);
let secondElement = numbers3.getElementAt(1);
console.log(secondElement);
