my_set = {"a", "b", "c", "c"}
print(my_set)
my_list = [1, 2, 3, 4]
my_set = set(my_list)
print(my_set)
print("a" in my_set)

for item in my_set:
    print(item)

my_set.add('d')
print(my_set)

my_set.update(['a', 'd', 'e', 'f'])
print(my_set)

my_set.remove('a')
print(my_set)

my_set.discard('b')
print(my_set)

my_set.pop()
print(my_set)


first_set = {'one', 'two', 'three'}
second_set = {'orange', 'banana', 'peach', 'one'}

print("集合并集")
print(first_set.union(second_set))
print(first_set)

print("集合交集")
print(first_set.intersection(second_set))
print(first_set)

first_set = {'one', 'two', 'three'}
second_set = {'three', 'four', 'one'}
print("集合差集")
print(first_set.difference(second_set))
print(second_set.difference(first_set))
print(first_set)
