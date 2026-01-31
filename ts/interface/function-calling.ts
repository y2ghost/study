interface Person {
  name: string;
  age: number;
}

function displayPerson(person: Person): void {
  console.log(person);
}

// 传递对象只要全部属性符合接口的要求就没有问题，不多不少
displayPerson({ age: 29, name: "yy" });
