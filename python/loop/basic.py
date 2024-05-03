my_list = [1, 2, 3]
for item in my_list:
    print(f'item: {item}, double: {item * 2}')
    if item % 2 == 0:
        print(f'{item} is even')

my_str = 'abcdefg'
for letter in my_str:
    print(letter)

for pos, letter in enumerate(my_str):
    print(f'{pos} -> {letter}')

books = {'y1': 'book1', 'y2': 'book2', 'y3': 'book3', 'y4': 'book4'}
for book in books:
    print(book)

for name, title in books.items():
    print(f'book: {name} - {title}')

list_of_tuples = [(1, 'banana'), (2, 'apple'), (3, 'pear')]
for number, fruit in list_of_tuples:
    print(f'{number} - {fruit}')


count = 0
while count < 10:
    if 4 == count:
        print(f'{count=}')
        break
    print(count)
    count += 1

for number in range(2, 12):
    if number % 2 == 0:
        continue
    print(number)
