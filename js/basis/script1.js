// 变量和值
{
  let js = "非常好玩";
  console.log(js);
  console.log(40 + 8 + 23 - 10);

  console.log("ydoit");
  console.log(23);

  let currentDateTime = "2023-06-25 22:22:22";
  console.log(currentDateTime);
}

// 变量名示例
{
  let language = "Javascript";
  let $function = 27;
  let person = "张三";
  let PI = 3.1415;
  let myFirstJob = "Coder";
  let myCurrentJob = "Teacher";
  let job1 = "programmer";
  let job2 = "teacher";
  console.log(myFirstJob);
}

// 数据类型示例
{
  let javascriptIsFun = true;
  console.log(javascriptIsFun);

  console.log(typeof true);
  console.log(typeof javascriptIsFun);
  console.log(typeof 23);
  console.log(typeof "string");

  javascriptIsFun = "YES!";
  console.log(typeof javascriptIsFun);

  let year;
  console.log(year);
  console.log(typeof year);

  year = 2023;
  console.log(typeof year);
  console.log(typeof null);
}

// let, const, var区别示例
{
  // let块作用域
  let age = 30;
  age = 31;

  // const定义不可变变量
  const birthYear = 2023;

  // var函数作用域
  var job = "programmer";
  job = "teacher";
}

// 基本运算示例
{
  const dateNow = 2023;
  const age1 = dateNow - 2010;
  const age2 = dateNow - 2018;
  console.log(age1, age2);
  console.log(age1 * 2, age2 / 10, 2 ** 3);

  const firstName = "firstName";
  const lastName = "lastName";
  console.log(firstName + " " + lastName);
}

// 赋值示例
{
  let x = 10 + 5; // 15
  x += 10; // x = x + 10 = 25
  x *= 4; // x = x * 4 = 100
  x++; // x = x + 1
  x--;
  x--;
  console.log(x);
}

// 比较示例
{
  const now = 2038;
  const age1 = now - 2010;
  const age2 = now - 2018;
  console.log(age1 > age2); // >, <, >=, <=
  console.log(age2 >= 18);

  const isFullAge = age2 >= 18;
  console.log(now - 2010 > now - 2018);
}

// 运算符优先级
{
  const now = 2038;
  const age1 = now - 2010;
  const age2 = now - 2018;

  console.log(now - 2010 > now - 2018);

  let x, y;
  x = y = 25 - 10 - 5; // x = y = 10, x = 10
  console.log(x, y);

  const averageAge = (age1 + age2) / 2;
  console.log(age1, age2, averageAge);
}

// 字符串和模板示例
{
  const firstName = "zhang";
  const job = "teacher";
  const birthYear = 2010;
  const year = 2038;
  const zhangsan =
    "I'm " + firstName + ", a " + (year - birthYear) + " year old " + job + "!";
  console.log(zhangsan);
  const zhangsan2 = `I'm ${firstName}, a ${year - birthYear} year old ${job}!`;
  console.log(zhangsan2);
  console.log(`Just a regular string...`);
  console.log(
    "String with \n\
multiple \n\
lines"
  );
  console.log(`String
multiple
lines`);
}

// 条件语句示例
{
  const age = 15;
  if (age >= 18) {
    console.log("ydoit can start driving license 🚗");
  } else {
    const yearsLeft = 18 - age;
    console.log(`ydoit is too young. Wait another ${yearsLeft} years :)`);
  }

  const birthYear = 2012;
  let century;
  if (birthYear <= 2000) {
    century = 20;
  } else {
    century = 21;
  }
  console.log(century);
}

// 类型转换
{
  // 显示转换
  const inputYear = "1991";
  console.log(Number(inputYear), inputYear);
  console.log(Number(inputYear) + 18);
  console.log(Number("invalid"));
  console.log(typeof NaN);
  console.log(String(23), 23);

  // 强制转换
  console.log("I am " + 23 + " years old");
  console.log("23" - "10" - 3);
  console.log("23" / "2");
  let n = "1" + 1; // '11'
  n = n - 1;
  console.log(n);
}

// 5个假值: 0, '', undefined, null, NaN
{
  console.log("真假判断示例");
  console.log(Boolean(0));
  console.log(Boolean(undefined));
  console.log(Boolean("true"));
  console.log(Boolean({}));
  console.log(Boolean(""));

  const money = 100;
  if (money) {
    console.log("Don't spend it all ;)");
  } else {
    console.log("You should get a job!");
  }

  let height = 0;
  if (height) {
    console.log("YAY! Height is defined");
  } else {
    console.log("Height is UNDEFINED");
  }
}

// 相等判断操作符 == vs. ===
{
  const age = "18";
  if (age === 18) console.log("You just became an adult :D (strict)");

  if (age == 18) console.log("You just became an adult :D (loose)");

  const favourite = Number(prompt("What's your favourite number?"));
  console.log(favourite);
  console.log(typeof favourite);

  if (favourite === 23) {
    // 22 === 23 -> FALSE
    console.log("Cool! 23 is an amzaing number!");
  } else if (favourite === 7) {
    console.log("7 is also a cool number");
  } else if (favourite === 9) {
    console.log("9 is also a cool number");
  } else {
    console.log("Number is not 23 or 7 or 9");
  }

  if (favourite !== 23) console.log("Why not 23?");
}

// 逻辑运算
{
  const hasDriversLicense = true;
  const hasGoodVision = true;
  console.log(hasDriversLicense && hasGoodVision);
  console.log(hasDriversLicense || hasGoodVision);
  console.log(!hasDriversLicense);

  const isTired = false;
  console.log(hasDriversLicense && hasGoodVision && isTired);

  if (hasDriversLicense && hasGoodVision && !isTired) {
    console.log("ydoit is able to drive!");
  } else {
    console.log("Someone else should drive...");
  }
}

// switch语句是
{
  const day = "friday";

  switch (day) {
    case "monday": // day === 'monday'
      console.log("Plan course structure");
      console.log("Go to coding meetup");
      break;
    case "tuesday":
      console.log("Prepare theory videos");
      break;
    case "wednesday":
    case "thursday":
      console.log("Write code examples");
      break;
    case "friday":
      console.log("Record videos");
      break;
    case "saturday":
    case "sunday":
      console.log("Enjoy the weekend :D");
      break;
    default:
      console.log("Not a valid day!");
  }

  if (day === "monday") {
    console.log("Plan course structure");
    console.log("Go to coding meetup");
  } else if (day === "tuesday") {
    console.log("Prepare theory videos");
  } else if (day === "wednesday" || day === "thursday") {
    console.log("Write code examples");
  } else if (day === "friday") {
    console.log("Record videos");
  } else if (day === "saturday" || day === "sunday") {
    console.log("Enjoy the weekend :D");
  } else {
    console.log("Not a valid day!");
  }
}

// 表达式示例
{
  3 + 4;
  1991;
  true && false && !false;

  if (23 > 10) {
    const str = "23 is bigger";
  }

  const me = "ydoit";
  console.log(`I'm ${2037 - 1991} years old ${me}`);

  const age = 23;
  const drink = age >= 18 ? "wine 🍷" : "water 💧";
  console.log(drink);

  let drink2;
  if (age >= 18) {
    drink2 = "wine 🍷";
  } else {
    drink2 = "water 💧";
  }

  console.log(drink2);
  console.log(`I like to drink ${age >= 18 ? "wine 🍷" : "water 💧"}`);
}
