class info:
    def __init__(self, arg1, arg2):
        print('执行 __init__')
        self.arg1 = arg1
        self.arg2 = arg2
        print('装饰器 args: {}, {}'.format(arg1, arg2))

    def __call__(self, func):
        print('调用 __call__')

        def wrapper(*args, **kwargs):
            print('调用 wrapper()')
            return func(*args, **kwargs)

        return wrapper


@info(3, 'Python')
def treble(number):
    return number * 3


print(treble(5))
