// IO操作示例

package main

import (
	"io/ioutil"
	"os"
)

func main() {
	name := "test.txt"
	txt := []byte("Not much in this file.")

	if err := ioutil.WriteFile(name, txt, 0755); err != nil {
		panic(err)
	}

	results, err := ioutil.ReadFile(name)
	if err != nil {
		panic(err)
	}

	println(string(results))
	reader, err := os.Open(name)

	if err != nil {
		panic(err)
	}

	results, err = ioutil.ReadAll(reader)
	if err != nil {
		panic(err)
	}

	reader.Close()
	println(string(results))
}
