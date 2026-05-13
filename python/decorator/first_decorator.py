def func_info(func):
    def wrapper():
        print('函数名称: ' + func.__name__)
        print('函数描述: ' + str(func.__doc__))
        result = func()
        return result
    return wrapper


def treble():
    return 3 * 3


new_treble = func_info(treble)
print(new_treble())
