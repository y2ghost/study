import re

# 测试字符集
re.findall('[abcde]', 'hello world!')
re.findall('[a-e]', 'hello world!')
re.findall('[a-eA-E0-4]', 'hello WORLD 42!')
re.findall('[^a-e]+', 'hello world')

# 匹配函数示例
text = 'More with less'
re.match('More', text)
re.fullmatch('More', text)

# 测试点号元字符
re.findall('M..', text)
re.findall('m..', text, flags=re.IGNORECASE)
s = '''hello
python'''
re.findall('o.p', s)
re.findall('o.p', s, flags=re.DOTALL)
re.findall('o[.\n]p', s)

# 测试星号元字符
text = 'food for fast and fun python learning'
re.findall('f.* ', text)
re.findall('f[a-z]*', text)
text = 'Python is ***great***'
re.findall('\\*', text)
re.findall('\\**', text)
re.findall('\\*+', text)

# 测试加号元字符
re.findall('a+b', 'aaaaaab')
re.findall('ab+', 'aaaaaabb')
re.findall('ab+', 'aaaaaabbbbb')
re.findall('ab+?', 'aaaaaabbbbb')
re.findall('ab+', 'aaaaaa')
re.findall('[a-z]+', 'hello world')

# 测试问号元字符
re.findall('aa[cde]?', 'aacde aa aadcde')
re.findall('aa?', 'accccacccac')
re.findall('aa[cde]?', 'aacde aa aadcde')

# 测试量词功能
re.findall('a?', 'aaaa')
re.findall('a*', 'aaaa')
re.findall('a+', 'aaaa')
re.findall('a{3}', 'aaaa')
re.findall('a{1,2}', 'aaaa')

# 测试抑制量词功能
re.findall('a?', 'aaaa')
re.findall('a??', 'aaaa')
re.findall('a*', 'aaaa')
re.findall('a*?', 'aaaa')
re.findall('a+', 'aaaa')
re.findall('a+?', 'aaaa')

# 测试位置匹配功能
re.findall('^PYTHON', 'PYTHON is fun.')
text = """
python is good
python is slow
python is simple
"""
re.findall('^python', text, re.MULTILINE)
text = '''
Coding is fun
Python is fun
Games are fun
Agreed?
'''
re.findall('fun$', 'PYTHON is fun')
re.findall('fun$', text)
re.findall('fun$', text, flags=re.MULTILINE)

# 测试'|'元字符
text = 'Buy now: iPhone only $399 with free iPad'
re.findall('(iPhone|iPad)', text)

# 测试AND关系的正则表达式
pattern = '(?=.*hi)(?=.*you)'
re.findall(pattern, 'hi how are you?')
re.findall(pattern, 'you are how? hi!')

pattern = '(?=.*hi)(?=.*you).*'
re.findall(pattern, 'you fly high')

# 测试NOT关系的正则表达式
re.findall('[^a-m]', 'aaabbbaababmmmnoopmmaa')
re.findall('[a-z]+(?![a-z]+)', 'hello world')

# 测试捕获分组
re.search(r'(j.n) is \1','jon is jon')
m = re.search(r'(j.n)','jon is jon')
m.group(1)
re.search(r'(j..) (j..)\s+\2', 'jon jim jim')

pattern = '(?P<quote>["\']).*(?P=quote)'
text = 'She said "hi"'
print(re.search(pattern, text))

# 非捕获分组示例
re.search('(?:python|java) is great', 'python is great. java is great.')

# 常见正则字符串操作示例
string = 'fgffffgfgPythonfgisfffawesomefgffg'
re.split('[fg]+', string)

text = 'C++ is the best language. C++ rocks!'
re.sub('C\\+\\+', 'Python', text)

regex = re.compile('Py...n')
match = regex.search('Python is great')





