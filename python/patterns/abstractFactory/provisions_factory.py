from abc import ABC, abstractmethod 
from sport import SportType
from player_strategy import VarsityBaseballPlayers
from player_strategy import VarsityFootballPlayers
from player_strategy import IntramuralBaseballPlayers
from player_strategy import IntramuralFootballPlayers
from player_strategy import IntramuralVolleyballPlayers
from venue_strategy import Stadium, OpenField


class ProvisionsFactory(ABC):
    @abstractmethod
    def make_players(self, player_type):
        pass
    
    @abstractmethod
    def make_venue(self):
        pass


class VarsityFactory(ProvisionsFactory):
    def make_players(self, sport_type):
        match sport_type:
            case SportType.BASEBALL:
                return VarsityBaseballPlayers()
            case SportType.FOOTBALL:
                return VarsityFootballPlayers()
            case _:
                return None;
        
    def make_venue(self):
        return Stadium()


class IntramuralFactory(ProvisionsFactory):
    def make_players(self, sport_type):
        match sport_type:
            case SportType.BASEBALL:
                return IntramuralBaseballPlayers()
            case SportType.FOOTBALL:
                return IntramuralFootballPlayers()
            case SportType.VOLLEYBALL:
                return IntramuralVolleyballPlayers()
            case _:
                return None;    
        
    def make_venue(self):
        return OpenField()
