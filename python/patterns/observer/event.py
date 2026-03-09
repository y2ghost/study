import math
import random


class Event:
    def __init__(self, player_name, hit):
        self._player_name = player_name
        self._hit = hit
        
    @property
    def player_name(self):
        return self._player_name
    
    @property
    def hit(self):
        return self._hit


class EventMaker:
    def __init__(self):
        self._name_index = -1
        self._outs = 0
        self._player_names = ('Al', 'Beth', 'Carl', 'Donna', 'Ed',
                              'Fran', 'George', 'Heidi', 'Ivan')
    
    def next_event(self):
        if self._outs < 27:
            r = random.gauss(0.0, 1.75)
            hit = math.floor(abs(r))
            
            if hit > 4:
                hit = 4
                
            if hit == 0:
                self._outs += 1
                
            self._name_index = (self._name_index + 1)%9
            
            return Event(self._player_names[self._name_index], 
                         hit)
        else:
            return None
