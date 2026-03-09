from game_data import GameData


class FootballData(GameData):
    def __init__(self, football_info):
        self._football_info = football_info
        
    def report_venue(self):
        return self._football_info.info[0]
    
    def report_attendance(self):
        return self._football_info.info[1]

