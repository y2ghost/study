my_list = [1, 2, 3]
print(my_list)
list_of_strings = list('abc')
print(list_of_strings)

empty_list = []
another_empty_list = list()
print(empty_list)
print(another_empty_list)

my_list = list('abcc')
count = my_list.count('a')
print(count)
index = my_list.index('c')
print(index)

my_list.append(1)
print(my_list)
my_list.insert(0, 'first')
print(my_list)
print(len(my_list))

other_list = [4, 5, 6]
my_list.extend(other_list)
print(my_list)
combined = my_list+other_list
print(combined)

my_list.clear()
print(my_list)
my_list = [7, 8, 9, 10]
my_list.pop()
print(my_list)
my_list.remove(8)
print(my_list)
del my_list[1]
print(my_list)

my_list = [4, 10, 2, 1, 23, 9]
my_list.sort()
print(my_list)

my_list = [4, 10, 2, 1, 23, 9]
sorted_list = sorted(my_list)
print(my_list)
print(sorted_list)
print(my_list[1:3])
print(my_list[-2:])

new_list = my_list.copy()
print(new_list)
