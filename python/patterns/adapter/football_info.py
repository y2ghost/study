from game_data import Venue


class FootballInfo:
    def __init__(self):
        self._info = [Venue.STADIUM, 2000]
        
    @property
    def info(self):
        return self._info
    
