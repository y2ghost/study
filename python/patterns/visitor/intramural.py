from node import Node


class Intramural(Node):
    def __init__(self):
        self._sports = []
        self._halls  = []
        
    def add_sport(self, sport):
        self._sports.append(sport)
        
    def add_hall(self, hall):
        self._halls.append(hall)
        
    def accept(self, visitor):
        visitor.visit_intramural(self)
        
        for sport in self._sports:
            sport.accept(visitor)
            
        for hall in self._halls:
            hall.accept(visitor)
