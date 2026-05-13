// 演示模块的使用示例
// 可以导入其他模块，同时自身也可以被当作模块使用
import * as parser from "./TestLib.js";

export function displayString(id, str) {
  var s = parser.split(str);
  s = parser.toHtmlLines(s);
  document.getElementById(id).innerHTML = s;
}

document.addEventListener("DOMContentLoaded", function (event) {
  displayString("display-div", "one,two,three");
});
