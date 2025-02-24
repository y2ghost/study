// errors示例

package main

import (
	"errors"
	"fmt"
)

func main() {
	err := errors.New("错误消息")
	fmt.Println(err)
}
