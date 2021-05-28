// 获取属性描述信息示例，通过Object.getOwnPropertyDescriptor(object, property)原型方法获取
// 描述对象属性说明:
// value: 属性值
// writable: 是否可写
// configurable: 是否可写和删除
// enumerable: 是否可以循环处理
// get: getter方法
// set: setter方法
const person = { name: "yy" };
let propertyDescriptor = Object.getOwnPropertyDescriptor(person, "name");
console.log(propertyDescriptor);

// 演示属性存在getter方法
const person2 = {
  get name() {
    return "Joe";
  },
};
propertyDescriptor = Object.getOwnPropertyDescriptor(person2, "name");
console.log(propertyDescriptor);
console.log(propertyDescriptor.get);
console.log(propertyDescriptor.get());

// 演示属性存在setter方法
const person3 = {
  set name(nm) {},
};
propertyDescriptor = Object.getOwnPropertyDescriptor(person3, "name");
console.log(propertyDescriptor);
console.log(propertyDescriptor.set);
