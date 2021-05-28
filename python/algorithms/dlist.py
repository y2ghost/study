class Node:
    def __init__(self, data=None, next=None, prev=None):
        self.data = data
        self.next = next
        self.prev = prev

    def __str__(self):
        return str(data)

class DoublyLinkedList:
    def __init__(self):
        self.size = 0
        # first item
        self.head = None
        # last item
        self.tail = None

    def append(self, data):
        node = Node(data, None, None)
        if self.head:
            node.prev = self.tail
            self.tail.next = node
            self.tail = node
        else:
            self.tail = node
            self.head = node

        self.size += 1

    def size(self):
        return self.size

    def iter(self):
        current = self.head
        while current:
            val = current.data
            current = current.next
            yield val

    def delete(self, data):
        current = self.head
        deleted = False

        if current is None:
            deleted = False
        elif current.data == data:
            self.head = current.next
            self.head.prev = None
            deleted = True
        elif self.tail.data == data:
            self.tail = self.tail.prev
            self.tail.next = None
            deleted = True
        else:
            while current:
                if current.data == data:
                    current.prev.next = current.next
                    current.next.prev = current.prev
                    deleted = True

                current = current.next

        if deleted:
            self.size -= 1

    def search(self, data):
        for node in self.iter():
            if data == node:
                return True

        return False

    def clear(self):
        self.tail = None
        self.head = None
        self.size = 0
