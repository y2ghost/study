# 时间复杂度分析
# enqueue O(1)
# pop  O(1)
# size O(1)
# peek O(1)
class Queue:
    def __init__(self):
        self.items = []
    def isEmpty(self):
        return self.items == []
    def enqueue(self, item):
        self.items.insert(0, item)
    def dequeue(self):
        return self.items.pop()
    def size(self):
        return len(self.items)

if '__main__' == __name__:
    queue = Queue()
    queue.enqueue('红')
    queue.enqueue('绿')
    queue.enqueue('蓝')
    queue.enqueue('黄')
    print(queue.size())
    print(queue.dequeue())
    print(queue.dequeue())
    print(queue.isEmpty())

