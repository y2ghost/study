def func_info(arg1, arg2):
    print('装饰器 arg1 = ' + str(arg1))
    print('装饰器 arg2 = ' + str(arg2))

    def the_real_decorator(func):
        def wrapper(*args, **kwargs):
            print('函数 {} args: {} kwargs: {}'
                  .format(
                      func.__name__,
                      str(args),
                      str(kwargs)))
            return func(*args, **kwargs)
        return wrapper

    return the_real_decorator


@func_info(3, 'Python')
def treble(number):
    return number * 3


print(treble(5))
