// 字符串操作示例

package main

import (
	"fmt"
	"strings"
)

func main() {
	fmt.Println(
		strings.Contains("test", "es"),
		strings.HasPrefix("test", "te"),
		strings.HasSuffix("test", "st"),
		strings.Count("test", "t"),
		strings.Index("test", "e"),
		strings.Join([]string{"input", "output"}, "/"),
		strings.Repeat("Golly", 6),
		strings.Replace("xxxx", "a", "b", 2),
		strings.Split("a-b-c-d-e", "-"),
		strings.ToLower("TEST"),
		strings.ToUpper("test"),
	)
}
