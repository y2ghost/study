// hash示例

package main

import (
	"fmt"
	"hash/crc32"
	"io"
	"os"
)

func hash(filename string) (uint32, error) {
	f, err := os.Open(filename)
	if err != nil {
		return 0, err
	}

	defer f.Close()
	h := crc32.NewIEEE()
	_, err = io.Copy(h, f)

	if err != nil {
		return 0, err
	}

	return h.Sum32(), nil
}

func main() {
	h1, err := hash("file1.txt")
	if err != nil {
		return
	}

	h2, err := hash("file2.txt")
	if err != nil {
		return
	}

	if h1 == h2 {
		fmt.Println(h1, h2, "校验正确")
	} else {
		fmt.Println(h1, h2, "校验失败")
	}
}
