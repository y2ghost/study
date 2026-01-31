// Goroutines示例

package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	go message("goroutine运行")
	message("正常运行")
	fmt.Printf("channel示例\n")
	channelDemo()
	printAccountNumber()
	multiChannelDemo()
}

func message(s string) {
	for i := 0; i < 5; i++ {
		time.Sleep(100 * time.Millisecond)
		fmt.Println(s)
	}
}

func broadcast(c chan int) {
	for i := 0; i < 5; i++ {
		c <- rand.Intn(999)
	}
}

func channelDemo() {
	// 无缓冲的channel
	// numbersStation := make(chan int)

	// 有缓存的channel
	numbersStation := make(chan int, 5)

	// 单独线程运行随机值生产函数
	go broadcast(numbersStation)

	// 打印随机值值
	for i := 0; i < 5; i++ {
		time.Sleep(1000 * time.Millisecond)
		fmt.Printf("%d ", <-numbersStation)
	}
}

func generateAccountNumber(accountNumberChannel chan int) {
	var accountNumber int
	accountNumber = 30000001

	for {
		// 循环5次后退出
		if accountNumber > 30000005 {
			close(accountNumberChannel)
			return
		}

		accountNumberChannel <- accountNumber
		accountNumber += 1
	}
}

func printAccountNumber() {
	accountNumberChannel := make(chan int)
	go generateAccountNumber(accountNumberChannel)

	newCustomers := []string{"张三", "李四", "王五",
		"老刘", "七儿", "八弟"}

	for _, newCustomer := range newCustomers {
		accnum, ok := <-accountNumberChannel
		if !ok {
			fmt.Printf("%s: 无账户ID可用\n", newCustomer)
		} else {
			fmt.Printf("%s: %d\n", newCustomer, accnum)
		}
	}
}

// 多channel操作示例
func multiBroadcast(nsChannel chan int, cChannel chan bool) {
	numbers := []int{
		101,
		102,
		103,
		104,
		105,
		106,
		107,
		108,
		109,
		110,
	}

	i := 0
	for {
		select {
		case nsChannel <- numbers[i]:
			i += 1
			if i == len(numbers) {
				i = 0
			}
		case <-cChannel:
			cChannel <- true
			return
		}
	}
}

func multiChannelDemo() {
	numbersStation := make(chan int)
	completeChannel := make(chan bool)
	go multiBroadcast(numbersStation, completeChannel)

	for i := 0; i < 100; i++ {
		time.Sleep(100 * time.Millisecond)
		fmt.Printf("%d ", <-numbersStation)
	}

	completeChannel <- true
	<-completeChannel
	fmt.Println("传输完成!")
}
