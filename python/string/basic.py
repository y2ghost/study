# 字符串的基本用法

# 分割
my_string = 'This is a string of words'
print(my_string.split())

# 格式化
name = 'yy'
print('My name is %s' % name)
age = 18
print('Hello %s. You must be at least %i to continue!' % (name, age))
print('Hello %(first_name)s. You must be at least %(age)i to continue!' %
      {"first_name": name, 'age': age})
print('Hello {}. You must be at least {} to continue!'.format(name, age))
print(f'Hello {name}. You are {age} years old')