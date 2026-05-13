// 控制流程示例

package main

import (
	"fmt"
	"os"
	"strconv"
)

func main() {
	if len(os.Args) < 5 {
		fmt.Printf("示例: go run control_flow.go args...\n")
		fmt.Printf("示例: go run control_flow.go 12 78 99 37\n")
		os.Exit(0)
	}

	val1 := os.Args[1]
	val2 := os.Args[2]
	val3 := os.Args[3]
	val4 := os.Args[4]

	fmt.Printf("%s %s %s %s\n", val1, val2, val3, val4)
	fmt.Printf("%s is %s\n", val1, oddOrEven(val1))
	fmt.Printf("%s is %s\n", val2, oddOrEven(val2))
	fmt.Printf("%s is %s\n", val3, oddOrEven(val3))
	fmt.Printf("%s is %s\n", val4, oddOrEven(val4))
	fmt.Printf("使用for循环的示例\n")

	for i := 1; i < len(os.Args); i++ {
		value := os.Args[i]
		fmt.Printf("%s is %s\n", value, oddOrEven(value))
	}

	numAtoI, numJtoR, numStoZ, numSpaces, numOther := 0, 0, 0, 0, 0
	sentence := "just test sentence"
	for i := 1; i < len(sentence); i++ {
		letter := string(sentence[i])
		switch letter {
		case "a", "b", "c", "d", "e", "f", "g", "h", "i":
			numAtoI += 1
		case "j", "k", "l", "m", "n", "o", "p", "q", "r":
			numJtoR += 1
		case "s", "t", "u", "v", "w", "x", "y", "z":
			numStoZ += 1
		case " ":
			numSpaces += 1
		default:
			numOther += 1
		}
	}

	fmt.Printf("打印字母统计情况\n")
	fmt.Printf("A to I: %d\n", numAtoI)
	fmt.Printf("J to R: %d\n", numJtoR)
	fmt.Printf("S to Z: %d\n", numStoZ)
	fmt.Printf("空白符: %d\n", numSpaces)
	fmt.Printf("其他字符: %d\n", numOther)
}

func oddOrEven(value string) string {
	num, _ := strconv.Atoi(value)
	if num%2 == 0 {
		return "even"
	} else {
		return "odd"
	}
}
