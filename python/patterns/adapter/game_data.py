from enum import Enum
from abc import ABC, abstractmethod, abstractclassmethod


class Venue(Enum):
    STADIUM = 0
    FIELD = 1


class GameData(ABC):       
    @abstractmethod
    def report_venue(self): pass
    
    @abstractmethod
    def report_attendance(self): pass

