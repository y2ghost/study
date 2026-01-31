# coding=utf8
# python cli执行如下命令:
# import debug_code
# import pdb
# pdb.run('debug_code.looper(5)')
# continue
# 也可以通过shell执行
# python -m pdb debug_code.py

def log(number):
    print(f'Processing {number}')
    print(f'Adding 2 to number: {number + 2}')


def looper(number):
    for i in range(number):
        log(i)


if __name__ == '__main__':
    looper(5)
