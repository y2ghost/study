// 数组和字典数据类型示例

package main

import (
	"fmt"
	"sort"
)

func main() {
	total := 0
	apples := [5]int{109, 210, 995, 111, 121}

	for _, value := range apples {
		total += value
	}

	mean := total / len(apples)
	fmt.Printf("平均苹果数: %d\n", mean)

	fmt.Printf("整数数组示例\n")
	printIntArray()

	fmt.Printf("字符串数组示例\n")
	printStringArray()

	fmt.Printf("数组slice语法示例\n")
	sliceDemo()

	fmt.Printf("创建slice语法示例\n")
	makeSlice()

	fmt.Printf("map语法示例\n")
	mapDemo()
}

func printIntArray() {
	var myArray = [5][2]int{{0, 0}, {2, 4}, {1, 3}, {5, 7}, {6, 8}}
	for i := 0; i < 5; i++ {
		for j := 0; j < 2; j++ {
			fmt.Printf("myArray[%d][%d] = %d\n", i, j, myArray[i][j])
		}
	}
}

func printStringArray() {
	myArray := [...]string{"Apples", "Oranges", "Bananas"}
	fmt.Printf("初始化字符串数组: %v\n", myArray)
	modifyStringArray(myArray)
	fmt.Printf("已修改字符串数组: %v\n", myArray)
}

// 参数是拷贝的数组副本，所以无法修改原始数组
func modifyStringArray(arr [3]string) {
	arr[1] = "Strawberries"
	fmt.Printf("modifyStringArray函数内部: %v\n", arr)
}

// slice属于引用传值，也就是可以修改原始的数组对象
func sliceDemo() {
	fruits := []string{"apples", "oranges", "bananas", "kiwis"}
	fmt.Printf("%v\n", fruits[1:3])
	modifyBySlice(fruits)
	fmt.Printf("%v\n", fruits[0:2])
	fmt.Printf("%v\n", fruits[:3])
	fmt.Printf("%v\n", fruits[2:])
}

func modifyBySlice(fruits []string) {
	fruits[1] = "fix_apples"
}

// 创建新的slice
func makeSlice() {
	mySlice := make([]int, 0, 8)
	mySlice = append(mySlice, 1, 3, 5, 7, 9, 11, 13, 17)

	fmt.Printf("元素列表: %v\n", mySlice)
	fmt.Printf("个数: %d\n", len(mySlice))
	fmt.Printf("容量: %d\n", cap(mySlice))
	mySlice = append(mySlice, 19)
	fmt.Printf("元素列表: %v\n", mySlice)
	fmt.Printf("个数: %d\n", len(mySlice))
	fmt.Printf("容量: %d\n", cap(mySlice))
}

func mapDemo() {
	actor := map[string]int{
		"yy1": 43,
		"yy2": 53,
		"yy3": 79,
		"yy4": 43,
		"yy5": 56,
		"yy6": 75,
		"yy7": 44,
	}

	// 等价于下面的初始化代码
	// actor := make(map[string]int)
	// actor["yy1"] = 43
	// actor["yy2"] = 53
	// actor["yy3"] = 79
	// actor["yy4"] = 43
	// actor["yy5"] = 56
	// actor["yy6"] = 75
	// actor["yy7"] = 44

	fmt.Printf("yy1的年龄%d\n", actor["yy1"])
	fmt.Printf("yy5的年龄%d\n", actor["yy5"])
	fmt.Printf("yy7的年龄%d\n", actor["yy7"])

	// map对象是未排序的
	fmt.Printf("map循环示例\n")
	for key, value := range actor {
		fmt.Printf("%s: 年龄%d\n", key, value)
	}

	fmt.Printf("map排序示例\n")
	mapSort(actor)

}

func mapSort(actor map[string]int) {
	var sortedActor []string
	for key := range actor {
		sortedActor = append(sortedActor, key)
	}

	sort.Strings(sortedActor)
	for _, name := range sortedActor {
		fmt.Printf("%s: 年龄%d\n", name, actor[name])
	}
}
