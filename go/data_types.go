// 数据类型示例
package main

import (
	"fmt"
	"reflect"
	"strconv"
)

func main() {
	// 整数类型
	int1 := 3
	int2 := 4
	fp1 := 22.0
	fp2 := 7.0
	fpResult := fp1 / fp2

	fmt.Printf("%d + %d = %d\n", int1, int2, int1+int2)
	fmt.Printf("%f / %f = %f\n", fp1, fp2, fpResult)
	fmt.Printf("fp1类型: %s\n", reflect.TypeOf(fp1))
	fmt.Printf("fp2类型: %s\n", reflect.TypeOf(fp2))
	fmt.Printf("fpResult类型: %s\n", reflect.TypeOf(fpResult))

	// bool类型
	var trueValue bool
	trueValue = true
	falseValue := false
	fmt.Println(trueValue && trueValue)
	fmt.Println(trueValue && falseValue)
	fmt.Println(trueValue || trueValue)
	fmt.Println(trueValue || falseValue)
	fmt.Println(!trueValue)

	// string类型
	str1 := "TAB前面\tTAB后面"
	str2 := `没法TAB分割\t开来`
	fmt.Println(str1)
	fmt.Println(str2)
	fmt.Println(str1 + str2)
	fmt.Println(len(str1))
	fmt.Println(str1[0:4])
	fmt.Println(str1 > str2)

	// 整数和字符串转换
	int1 = 48
	int1_as_string := strconv.Itoa(int1)
	str1 = "我的数字"
	str1 = str1 + int1_as_string
	fmt.Println(str1)

	str2 = "4"
	str2_as_integer, _ := strconv.Atoi(str2)
	intResult := 48 / str2_as_integer
	fmt.Printf("%s / %s is %d\n", int1_as_string, str2, intResult)
}
