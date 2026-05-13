// 演示变量全局作用域

package main

import (
	"fmt"
)

const (
	prizeDay1  = "星期1"
	prizeFund1 = 10000
)

var (
	prizeDay2  = "星期2"
	prizeFund2 = 20000
)

func main() {
	msg := "%s的获奖作品是%d号，奖金%d!!!\n"
	winner := 9
	fmt.Printf(msg, prizeDay1, winner, prizeFund2)

	prizeDay2 = "星期5"
	prizeFund2 = 50000
	winner = 10
	fmt.Printf(msg, prizeDay2, winner, prizeFund2)
}
