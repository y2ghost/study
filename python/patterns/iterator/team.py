from abc import ABC, abstractmethod
from collections import namedtuple
from player import Player
from iterator import ListIterator, GeneratorIterator
from iterator import DictionaryIterator


class Team(ABC):
    def __init__(self, name, players):
        self._name = name
        self._players = players
        
    @property
    def name(self):
        return self._name
    
    @property
    def players(self):
        return self._players
    
    @property
    @abstractmethod
    def iterator(self):
        pass
    

class Team_1(Team):
    def __init__(self):
        super().__init__('TEAM 1', [
            (12436, 'Alwin', 'Jim'),
            (26410, 'Bond', 'Bob'),
            (14306, 'Charles', 'Ronda'),
            (61835, 'Dunn', 'Fred'),
            (30437, 'Edwards', 'Gina'),
            (76517, 'Fanning', 'Pat'),
            (98734, 'Galway', 'Leslie'),
            (14998, 'Hiroshi', 'Scott'),
            (47303, 'Ingles', 'Mary')
        ])
        
        self._iterator = ListIterator(self)
    
    @property
    def iterator(self):
        return self._iterator
    

class Team_2(Team):
    def __init__(self):
        super().__init__('TEAM 2', [
            (63421, 'Jackson', 'Tammy'),
            (44551, 'Killebrew', 'Wally'),
            (14306, 'Lamprey', 'Roberta'),
            (61835, 'Mays', 'Serena'),
            (30437, 'Norton', 'Donna'),
            (76517, 'OBrien', 'George'),
            (98734, 'Paulson', 'Marsha'),
            (14998, 'Quark', 'John'),
            (47303, 'Rogers', 'Jena')
        ])
        
        self._iterator = GeneratorIterator(self)
        self._index = -1        
        
    def __iter__(self):
        return self
    
    def __next__(self):
        if self._index < len(self._players) - 1:
            self._index += 1
            return Player(*self._players[self._index])
        else:
            raise StopIteration
    
    @property
    def iterator(self):
        return self._iterator
    

class Team_3(Team):
    def __init__(self):
        super().__init__('TEAM 3', {})
        Name = namedtuple('Name', ['last_name', 'first_name'])
        
        self._players[46841] = Name( last_name='Smith', 
                                    first_name='Ken')
        self._players[98765] = Name( last_name='Terrance', 
                                    first_name='Laura')
        self._players[10547] = Name( last_name='Ulster', 
                                    first_name='Doug')
        self._players[38331] = Name( last_name='Vicks', 
                                    first_name='Ron')
        self._players[47781] = Name( last_name='Wong', 
                                    first_name='Henrietta')
        self._players[57974] = Name( last_name='Xavier', 
                                    first_name='Nancy')
        self._players[56712] = Name( last_name='Yonnick', 
                                    first_name='Billy')
        self._players[72288] = Name( last_name='Aaron', 
                                    first_name='Patricia')
        
        self._iterator = DictionaryIterator(self)
    
    @property
    def iterator(self):
        return self._iterator
