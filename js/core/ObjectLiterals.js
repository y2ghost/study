let task = {
  name: "async task",
  // 支持类语法定义属性名为start的函数
  start() {
    console.log("running " + this.name);
  },
};

console.log(task);
task.start();

// 属性和变量名称相同
let name = "yy";
let phone = "111-111-111";
let person = { name, phone, age: 30 };
console.log(person);

// 动态属性名称
let personId = "name";
phone = "111-111-111";
let phoneType = "Cell";
person = {
  [personId]: "yy",
  [phoneType + "-Phone"]: phone,
};
console.log(person);

// 动态函数名称
let funcName = "doSomething";
let t = {
  [funcName]: function () {
    console.log("doing something");
  },
};

t.doSomething();

let funcName2 = "Display";
let t2 = {
  ["do" + funcName2]() {
    console.log("displaying");
  },
};

t2.doDisplay();
