class Subject:
    def __init__(self):
        self._observers = []
        
    def attach(self, observer):
        self._observers.append(observer)
        
    def detach(self, observer):
        self._observers.remove(observer)
        
    def _notify(self, player_name):
        for obs in self._observers:
            obs.update(player_name)

