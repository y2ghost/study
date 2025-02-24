// 用于演示模块的功能测试库
// 定义的变量不能直接被外部使用
const SPLIT_CHAR = ",";

export function split(str) {
  return str.split(SPLIT_CHAR);
}

export function toHtmlLines(strArray) {
  return strArray.reduce((s1, s2) => join(s1, s2, "<br>"));
}

// 没被导出，同样不能被外部使用
function join(s1, s2, separator) {
  return s1 + separator + s2;
}
