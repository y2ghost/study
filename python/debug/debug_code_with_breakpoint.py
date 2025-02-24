# 使用断点函数调试

def log(number):
    print(f'Processing {number}')
    print(f'Adding 2 to number: {number + 2}')


def looper(number):
    for i in range(number):
        breakpoint()
        log(i)


if __name__ == '__main__':
    looper(5)
