def bold(func):
    def wrapper():
        return "<b>" + func() + "</b>"
    return wrapper


def italic(func):
    def wrapper():
        return "<i>" + func() + "</i>"
    return wrapper


# 装饰器函数是自底向上的顺序
@bold
@italic
def formatted_text():
    return '格式化文本!'


print(formatted_text())
