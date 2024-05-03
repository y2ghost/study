# 时间复杂度分析
# push O(1)
# pop  O(1)
# size O(1)
# peek O(1)
class Stack:
    def __init__(self):
        self.items = []
    def isEmpty(self):
        return self.items == []
    def push(self, item):
        self.items.append(item)
    def pop(self):
        return self.items.pop()
    def peek(self):
        return self.items[len(self.items)-1]
    def size(self):
        return len(self.items)

if '__main__' == __name__:
    stack = Stack()
    stack.push('红')
    stack.push('绿')
    stack.push('蓝')
    stack.push('黄')
    print(stack.pop())
    print(stack.isEmpty())

