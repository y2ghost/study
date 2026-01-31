authenticated = False
if authenticated:
    print('You are logged in')
else:
    print('Please login')

age = 10
if age < 18:
    print('You can follow the elections on the news')
elif age < 35:
    print('You can vote in all elections')
elif age >= 35:
    print('You can stand for any election')

color = 'white'
if age <= 14 and (color == 'white' or color == 'green'):
    print(f'This milk is {age} days old and looks {color}')
else:
    print(f'You should not drink this {age} day old milk...')

x = [1, 2, 3]
y = [1, 2, 3]
print(x == y)
print(x is y)

x = 'hello world'
y = 'hello world'
print(x == y)
print(x is y)

valid_chars = 'yn'
char = 'x'
if char in valid_chars:
    print(f'{char} is a valid character')
else:
    print(f'{char} is not in {valid_chars}')

my_list = [1, 2, 3, 4]
print(5 not in my_list)

ids = [1234, 5678]
my_id = 1001
if my_id not in ids:
    print('You are not authorized!')
