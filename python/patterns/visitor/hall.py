from enum import Enum
from node import Node


class HallName(Enum):
    NORTH = 1
    SOUTH = 2
    EAST  = 3
    WEST  = 4
    
    def __str__(self): 
        return (self.name.lower().capitalize() + ' Hall')


class Hall(Node):
    def __init__(self, name):
        self._name = name
        self._win_count = 0
        
    @property
    def name(self):
        return self._name
    
    @property
    def win_count(self):
        return self._win_count
        
    def increment_win_count(self):
        self._win_count += 1

    def accept(self, visitor):
        visitor.visit_hall(self)
        
