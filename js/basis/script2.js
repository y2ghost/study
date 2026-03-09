// 使用严格模式
"use strict";

{
  let hasDriversLicense = false;
  const passTest = true;
  // 严格模式下不行
  // const interface = "编译通不过!";

  if (passTest) {
    hasDriversLicense = true;
  }

  if (hasDriversLicense) {
    console.log("我的驾照是C1D");
  }
}

// 函数示例
{
  function logger() {
    console.log("我的名字是杨小九");
  }

  logger();
  logger();
  logger();

  function fruitProcessor(apples, oranges) {
    const juice = `Juice with ${apples} apples and ${oranges} oranges.`;
    return juice;
  }

  const appleJuice = fruitProcessor(5, 0);
  console.log(appleJuice);

  const appleOrangeJuice = fruitProcessor(2, 4);
  console.log(appleOrangeJuice);
  const num = Number("23");
}

// 函数定义和表达式
{
  // 函数定义
  function calcAge1(birthYear) {
    return 2037 - birthYear;
  }

  const age1 = calcAge1(1991);

  // 函数表达式
  const calcAge2 = function (birthYear) {
    return 2037 - birthYear;
  };

  const age2 = calcAge2(1991);
  console.log(age1, age2);

  // 箭头函数
  const calcAge3 = (birthYear) => 2037 - birthYear;
  const age3 = calcAge3(1991);
  console.log(age3);

  const yearsUntilRetirement = (birthYear, firstName) => {
    const age = 2037 - birthYear;
    const retirement = 65 - age;
    return `${firstName} retires in ${retirement} years`;
  };

  console.log(yearsUntilRetirement(1991, "张三"));
  console.log(yearsUntilRetirement(1980, "李四"));
}

// 函数调用
{
  function cutFruitPieces(fruit) {
    return fruit * 4;
  }

  function fruitProcessor(apples, oranges) {
    const applePieces = cutFruitPieces(apples);
    const orangePieces = cutFruitPieces(oranges);
    const juice = `Juice with ${applePieces} piece of apple and ${orangePieces} pieces of orange.`;
    return juice;
  }

  console.log(fruitProcessor(2, 3));

  const calcAge = function (birthYear) {
    return 2037 - birthYear;
  };

  const yearsUntilRetirement = function (birthYear, firstName) {
    const age = calcAge(birthYear);
    const retirement = 65 - age;

    if (retirement > 0) {
      console.log(`${firstName} retires in ${retirement} years`);
      return retirement;
    } else {
      console.log(`${firstName} has already retired 🎉`);
      return -1;
    }
  };

  console.log(yearsUntilRetirement(1991, "张三"));
  console.log(yearsUntilRetirement(1950, "李四"));
}

// 数组示例
{
  const friend1 = "张三";
  const friend2 = "李四";
  const friend3 = "王五";

  const friends = ["张三", "李四", "王五"];
  console.log(friends);

  const y = new Array(1991, 1984, 2008, 2020);

  console.log(friends[0]);
  console.log(friends[2]);

  console.log(friends.length);
  console.log(friends[friends.length - 1]);

  friends[2] = "老二";
  console.log(friends);

  // 添加元素
  const newLength = friends.push("老刘");
  console.log(friends);
  console.log(newLength);

  friends.unshift("老刘");
  console.log(friends);

  // 删除元素
  friends.pop();
  const popped = friends.pop();
  console.log(popped);
  console.log(friends);

  friends.shift();
  console.log(friends);

  console.log(friends.indexOf("张三"));
  console.log(friends.indexOf("李四"));

  friends.push(23);
  console.log(friends.includes("张三"));
  console.log(friends.includes("李四"));
  console.log(friends.includes(23));

  if (friends.includes("李四")) {
    console.log("你有个朋友叫李四");
  }
}

// 对象示例
{
  const zhangsan = {
    name: "张三",
    age: 2037 - 2011,
    job: "teacher",
    friends: ["李四", "王五", "陈六"],
  };

  const lisi = {
    name: "李四",
    age: 2037 - 2010,
    job: "teacher",
    friends: ["李四", "王五", "陈六"],
  };

  console.log(zhangsan);
  console.log(lisi.name);
  console.log(lisi["name"]);

  const nameKey = "name";
  console.log(zhangsan[nameKey]);
  console.log(lisi[nameKey]);

  // 添加属性
  lisi.location = "China";
  lisi["twitter"] = "@lisi";
  console.log(lisi);
}

// 对象函数示例
{
  const zhangsan = {
    name: "张三",
    birthYear: 2011,
    job: "teacher",
    friends: ["李四", "王五", "陈六"],

    calcAge: function () {
      this.age = 2037 - this.birthYear;
      return this.age;
    },
  };

  console.log(zhangsan.calcAge());
  console.log(zhangsan.age);
  console.log(zhangsan.age);
  console.log(zhangsan.age);
}

// 循环示例
{
  for (let rep = 1; rep <= 30; rep++) {
    console.log(`Lifting weights repetition ${rep} 🏋️‍♀️`);
  }

  const lisi = ["李四", 2037 - 1991, "teacher", ["张三", "王五", "小六"], true];
  const types = [];

  for (let i = 0; i < lisi.length; i++) {
    console.log(lisi[i], typeof lisi[i]);
    types.push(typeof lisi[i]);
  }

  console.log(types);
  const years = [1991, 2007, 1969, 2020];
  const ages = [];

  for (let i = 0; i < years.length; i++) {
    ages.push(2037 - years[i]);
  }
  console.log(ages);
  console.log("--- 仅仅过滤字符串 ---");

  for (let i = 0; i < lisi.length; i++) {
    if (typeof lisi[i] !== "string") {
      continue;
    }

    console.log(lisi[i], typeof lisi[i]);
  }

  console.log("--- 数字中断 ---");
  for (let i = 0; i < lisi.length; i++) {
    if (typeof lisi[i] === "number") {
      break;
    }

    console.log(lisi[i], typeof lisi[i]);
  }

  const lisi2 = [
    "李四",
    2037 - 1991,
    "teacher",
    ["张三", "王五", "小六"],
    true,
  ];

  for (let i = lisi2.length - 1; i >= 0; i--) {
    console.log(i, lisi2[i]);
  }

  for (let rep = 1; rep <= 10; rep++) {
    console.log(`Lifting weights repetition ${rep} 🏋️‍♀️`);
  }

  let rep = 1;
  while (rep <= 10) {
    console.log(`WHILE: Lifting weights repetition ${rep} 🏋️‍♀️`);
    rep++;
  }

  let dice = Math.trunc(Math.random() * 6) + 1;
  while (dice !== 6) {
    console.log(`You rolled a ${dice}`);
    dice = Math.trunc(Math.random() * 6) + 1;
    if (dice === 6) console.log("Loop is about to end...");
  }
}

