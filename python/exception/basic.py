# 基本示例
try:
    with open('example.txt') as file_handler:
        for line in file_handler:
            print(line)
except OSError:
    print('An error occurred')

# 抛出异常
try:
    raise ImportError
except ImportError:
    print('Caught an ImportError')

# 处理异常示例
try:
    raise ImportError("Bad import")
except ImportError as error:
    print(type(error))
    print(error.args)
    print(error)

try:
    print('This is the try block')
except IOError:
    print('An IOError has occurred')
else:
    print('This is the else block')

try:
    1 / 0
finally:
    print("cleaning up")
