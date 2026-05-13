class Node:
    def __init__(self, data=None):
        self.data = data
        self.next = None

    def __str__(self):
        return str(data)

class SinglyLinkedList:
    def __init__(self):
        self.size = 0
        # last item
        self.head = None
        # first item
        self.tail = None

    def append(self, data):
        node = Node(data)
        if self.head:
            self.head.next = node
            self.head = node
        else:
            self.tail = node
            self.head = node

        self.size += 1

    def size(self):
        return self.size

    def iter(self):
        current = self.tail
        while current:
            val = current.data
            current = current.next
            yield val

    def delete(self, data):
        current = self.tail
        prev = self.tail

        while current:
            if current.data == data:
                if current == self.tail:
                    self.tail = current.next
                else:
                    prev.next = current
                
                self.size -= 1
                break
            
            prev = current
            current = current.next

    def search(self, data):
        for node in self.iter():
            if data == node:
                return True

        return False

    def clear(self):
        self.tail = None
        self.head = None
        self.size = 0
