from enum import Enum
from node import Node


class SportType(Enum):
    BASEBALL   = 1
    FOOTBALL   = 2
    VOLLEYBALL = 3
    
    def __str__(self): 
        return self.name.lower()


class Sport(Node):
    def __init__(self, sport_type):
        self._type = sport_type
        self._games = []

    @property
    def type(self):
        return self._type
        
    @property
    def games_count(self):
        return len(self._games)
    
    def add_game(self, game):
        self._games.append(game)
    
    def accept(self, visitor):
        visitor.visit_sport(self)
        
        for game in self._games:
            game.accept(visitor)

