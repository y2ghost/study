def my_function():
    print('Hello from my_function')


def welcome(name):
    print(f'Welcome {name}')


def welcome2(name: str) -> None:
    print(f'Welcome {name}')


def welcome3(name: str, age: int = 15) -> None:
    print(f'Welcome {name}, your are {age} years old')


def any_args(*args):
    print(args)


def one_required_args(required, *args):
    print(f'{required=}')
    print(args)


def any_keyword_args(**kwargs):
    print(kwargs)


def arg_inspector(*args, **kwargs):
    print(f'args are of type {type(args)}')
    print(f'kwargs are of type {type(kwargs)}')


def show_output(*args, **kwargs):
    print(f'{args=}')
    print(f'{kwargs=}')

# 位置固定的参数


def positional(name, age, /, **kwargs):
    print(f'{name=}')
    print(f'{age=}')
    print(f'{kwargs=}')


my_function()
welcome('yy')
welcome2('yy2')
welcome3('yy3')
any_args(1, 2, 3)
one_required_args('yy', 1, 2)
any_keyword_args(one=1, two=2, three=3)
arg_inspector(1, 2, 4, x='test', y=5)
my_tuple = (1, 2, 3)
my_dict = {'one': 1, 'two': 2}
show_output(my_tuple)
show_output(my_tuple, my_dict)
show_output(*my_tuple)
show_output(*my_tuple, **my_dict)
positional('yy', 17, name='y2')
