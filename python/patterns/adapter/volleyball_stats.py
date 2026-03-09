from game_data import Venue


class VolleyballStats:
    def __init__(self):
        self._stats = {
            'venue' : Venue.FIELD,
            'attendance' : 150
        }

    @property
    def stats(self):
        return self._stats
    
