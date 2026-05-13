// 导入模块默认的对象，此处名称自定义，并非模块内部默认对象的名称

import Something from "./DefaultLib.js";

export function displaySquare(id, number) {
  let squaredValue = Something.square(number);
  document.getElementById(id).innerHTML = squaredValue;
}
