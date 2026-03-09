from abc import ABC, abstractmethod


class Node(ABC):
    @abstractmethod
    def accept(self, visitor):
        pass

