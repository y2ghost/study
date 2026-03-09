sequence = [1, 2, 3]
new_list = [x for x in sequence]
print(new_list)

new_list = [x*2 for x in sequence]
print(new_list)

odd_numbers = [x for x in range(10) if x % 2]
print(odd_numbers)

my_dict = {1: 'dog', 2: 'cat', 3: 'python'}
my_list = [(num, animal)
           for num in my_dict
           for animal in my_dict.values()
           if my_dict[num] == animal]
print(my_list)

matrix = [[9, 8, 7], [6, 5, 4], [3, 2, 1]]
my_list = [[element*2 for element in row] for row in matrix]
print(my_list)

my_list = {key: value for key, value in enumerate('abcde')}
print(my_list)

my_list = list('aaabbcde')
my_set = {item for item in my_list}
print(my_set)
