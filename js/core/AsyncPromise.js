// 异步编程示例，通过构造Promise对象来支持
// 语法:
// let promise = new Promise(function (resolve, reject) {
//   if (someCondition) {
//     resolve("done");
//   } else {
//     reject("error");
//   }
// });
// 处理完毕后，需要回调对应的结果处理函数
// promise.then(function(result){}, function(error){});

let job = function (resolve, reject) {
  console.log("promise执行器开始任务");
  setTimeout(function () {
    if (Math.random() > 0.2) {
      resolve("done");
    } else {
      reject("failed");
    }
  }, 5000);
};

console.log("-- 创建异步支持promise对象 --");
let promise = new Promise(job);
console.log("-- 调用then函数确认结果 --");
promise
  .then(
    function (result) {
      console.log("成功执行: " + result);
    },
    function (error) {
      console.log("执行出错: " + error);
    }
  )
  .finally(function () {
    console.log("-- 无论结果如何，finally方法都会回调函数 --");
  });
console.log("-- 调用then结束 --");

// 使用Promise.catch只处理错误情况
// 语法: promise.catch(function(error){});
console.log("-- 创建异步支持promise对象 --");
promise = new Promise(job);
console.log("-- 调用catch函数确认结果 --");
promise.catch(function (error) {
  console.log("错误结果: " + error);
});
console.log("-- 调用catch函数结束 --");
