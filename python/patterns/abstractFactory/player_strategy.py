from abc import ABC, abstractmethod


class PlayerStrategy(ABC):
    @abstractmethod
    def strategy(self): pass


class VarsityBaseballPlayers(PlayerStrategy):
    def strategy(self):
        return 'varsity baseball players'


class VarsityFootballPlayers(PlayerStrategy):
    def strategy(self):
        return 'varsity football players'


class IntramuralBaseballPlayers(PlayerStrategy):
    def strategy(self):
        return 'intramural baseball players'


class IntramuralFootballPlayers(PlayerStrategy):
    def strategy(self):
        return 'intramural football players'


class IntramuralVolleyballPlayers(PlayerStrategy):
    def strategy(self):
        return 'intramural volleyball players'

