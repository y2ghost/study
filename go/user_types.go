// 用户自定义类型示例
package main

import (
	"fmt"
)

func main() {
	a, b := 20, 71

	// a的指针
	p := &a
	fmt.Printf("a的初始值: %d\n", *p)
	*p = 21
	fmt.Printf("a的修改值: %d\n", a)

	p = &b
	fmt.Printf("b的初始值: %d\n", *p)
	*p = *p + 10
	fmt.Printf("b的修改值: %d\n", b)

	fmt.Printf("struct类型示例\n")
	structDemo()

	fmt.Printf("interface类型示例\n")
	interfaceDemo()
}

type rect struct {
	height int
	width  int
}

func structDemo() {
	r := rect{height: 12, width: 20}
	fmt.Printf("高: %d\n宽: %d\n", r.height, r.width)
	fmt.Printf("面积: %d\n", r.area())

	r.double()
	fmt.Printf("调用double函数之后\n")
	fmt.Printf("高: %d\n宽: %d\n", r.height, r.width)
	fmt.Printf("面积: %d\n", r.area())

	fmt.Printf("嵌入类型示例\n")
	embeddedTypeDemo()
}

func (r rect) area() int {
	h := r.height
	w := r.width
	return h * w
}

func (r *rect) double() {
	r.height *= 2
	r.width *= 2
}

type Discount struct {
	percent     float32
	promotionId string
}

type ManagersSpecial struct {
	Discount
	extraoff float32
}

func embeddedTypeDemo() {
	normalPrice := float32(99.99)
	januarySale := Discount{15.00, "January"}
	managerSpecial := ManagersSpecial{januarySale, 10.00}
	discountedPrice := januarySale.Calculate(normalPrice)
	managerDiscount := managerSpecial.Calculate(normalPrice)

	fmt.Printf("原价: $%4.2f\n", normalPrice)
	fmt.Printf("折扣: %2.2f\n", januarySale.percent)
	fmt.Printf("折后价: $%4.2f\n", discountedPrice)
	fmt.Printf("经理折后价: $%4.2f\n", managerDiscount)
}

func (d Discount) Calculate(originalPrice float32) float32 {
	return originalPrice - (originalPrice / 100 * d.percent)
}

func (ms ManagersSpecial) Calculate(originalPrice float32) float32 {
	return ms.Discount.Calculate(originalPrice) - ms.extraoff
}

type Person struct {
	Name string
}

type Animal struct {
	Sound string
}

type Greeter interface {
	SayHi() string
}

func (p Person) SayHi() string {
	return "你好我的名字是" + p.Name
}

func (a Animal) SayHi() string {
	return a.Sound
}

func greet(i Greeter) {
	fmt.Println(i.SayHi())
}

func interfaceDemo() {
	man := Person{Name: "工具人"}
	dog := Animal{Sound: "狗叫声"}

	fmt.Println("\nPerson: ")
	greet(man)

	fmt.Println("\nAnimal: ")
	greet(dog)

	fmt.Println("\n空接口示例")
	emptyInterfaceExample()

	fmt.Println("\n类型推断示例")
	typeCheck()
}

func emptyInterfaceExample() {
	displayType(42)
	displayType(3.14)
	displayType("ここでは文字列です")
}

// 空接口能够容纳各种类型
func displayType(i interface{}) {
	switch theType := i.(type) {
	case int:
		fmt.Printf("整数类型: %d\n", theType)
	case float64:
		fmt.Printf("浮点数类型: %f\n", theType)
	case string:
		fmt.Printf("字符串类型: %s\n", theType)
	default:
		fmt.Printf("未知类型: %v\n", theType)
	}
}

// 类型推断示例
func typeCheck() {
	var anything interface{} = "something"
	aInt, ok := anything.(int)
	if !ok {
		fmt.Println("不是整数类型\n")
	} else {
		fmt.Println(aInt)
	}
}
