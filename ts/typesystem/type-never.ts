// never类型表示使用该类型的代码则永远不可访问

function loopForever(): never {
  while (true) {}
}

function terminateWithError($msg): never {
  throw new Error($msg);
}

// 类型检查例子
function checkExhaustiveness(x: never): never {
  throw new Error("exhaustive check fails for: " + x);
}

function showTruFalse(x: string | boolean): void {
  if (typeof x === "string") {
    console.log("string: " + x);
  } else if (typeof x === "boolean") {
    console.log("boolean: " + x);
  } else {
    checkExhaustiveness(x);
  }
}

showTruFalse(true);
showTruFalse("false");
