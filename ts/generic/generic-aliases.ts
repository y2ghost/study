// 泛化类型别名示例
type ListType<T> = { elements: T[] };
let numList: ListType<number> = { elements: [1, 2, 3, 4] };
console.log(numList);

// 泛化交集类型示例
type Entity<E> = { id: number } & E;

interface Item {
  name: string;
  price: number;
}

let itemEntity: Entity<Item> = { id: 1, name: "Laptop", price: 150 };
console.log(itemEntity);
