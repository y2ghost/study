class Player:
    def __init__(self, player_id, last, first):
        self._player_id = player_id
        self._last_name = last
        self._first_name = first
        
    @property
    def player_id(self):
        return self._player_id
    
    @property
    def last_name(self):
        return self._last_name
    
    @property
    def first_name(self):
        return self._first_name

