def bold(func):
    print(f'您使用粗体装饰 {func.__name__}')

    def bold_wrapper():
        return "<b>" + func() + "</b>"
    return bold_wrapper


def italic(func):
    print(f'您使用斜体装饰 {func.__name__}')

    def italic_wrapper():
        return "<i>" + func() + "</i>"
    return italic_wrapper

# 装饰器函数是自底向上的顺序


@bold
@italic
def formatted_text():
    return '格式化文本!'


print(formatted_text())
