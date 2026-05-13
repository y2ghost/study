def func_info(func):
    def wrapper(*args):
        print('函数名称: ' + func.__name__)
        print('函数描述: ' + str(func.__doc__))
        result = func(*args)
        return result
    return wrapper


@func_info
def treble(a: int) -> int:
    """使其输入增加三倍的功能"""
    return a * 3


print(treble(5))
