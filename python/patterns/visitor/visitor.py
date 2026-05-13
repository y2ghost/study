from abc import ABC, abstractmethod


class Visitor(ABC):
    @abstractmethod
    def visit_intramural(self, node):
        pass
    
    @abstractmethod
    def visit_sport(self, node):
        pass
    
    @abstractmethod
    def visit_game(self, node):
        pass
    
    @abstractmethod
    def visit_hall(self, node):
        pass

