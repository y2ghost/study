not_a_tuple = (5)
print(type(not_a_tuple))

a_tuple = 4, 5
print(type(a_tuple))

a_tuple = (2, 3, 4)
print(type(a_tuple))

a_tuple = tuple(['1', '2', '3'])
print(type(a_tuple))

a_tuple = (1, 2, 3, 3)
print(a_tuple.count(3))

a_tuple = (1, 2, 3, 3)
print(a_tuple.index(2))
print(a_tuple[2])

# 拼接tuple实际创建了新的tuple对象
print(id(a_tuple))
a_tuple += (6, 7)
print(id(a_tuple))
print(a_tuple)

empty = tuple()
print(f"empty tuple:{empty}, len:{len(empty)}, type: {type(empty)}")

also_empty = ()
print(
    f"empty tuple:{also_empty}, len:{len(also_empty)}, type: {type(also_empty)}")
