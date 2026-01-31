# 设置trace调试

def log(number):
    print(f'Processing {number}')
    print(f'Adding 2 to number: {number + 2}')


def looper(number):
    for i in range(number):
        import pdb
        pdb.set_trace()
        log(i)


if __name__ == '__main__':
    looper(5)
