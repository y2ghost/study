# 读取文件
_FILE_NAME = "example.txt"

with open(_FILE_NAME) as file_handler:
    for line in file_handler:
        print(line)

with open(_FILE_NAME) as file_handler:
    lines = file_handler.readlines()
    for line in lines:
        print(line)

with open(_FILE_NAME) as file_handler:
    file_contents = file_handler.read()
    print(file_contents)

with open(_FILE_NAME) as file_handler:
    while True:
        data = file_handler.read(1024)
        if not data:
            break
        print(data)

# 二进制文件读取
with open(_FILE_NAME, 'rb') as file_handler:
    file_contents = file_handler.read()
    print(file_contents)

# 写文件例子
with open(_FILE_NAME, 'w') as file_handler:
    file_handler.write('this is a test\n')

# 定位文件
with open(_FILE_NAME) as file_handler:
    file_handler.seek(4)
    chunk = file_handler.read()
    print(chunk)

# 追写文件例子
with open(_FILE_NAME, 'a') as file_handler:
    file_handler.write('this is a test\n')
