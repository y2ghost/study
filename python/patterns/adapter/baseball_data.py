from game_data import GameData, Venue


class BaseballData(GameData):
    def report_venue(self):
        return Venue.STADIUM
    
    def report_attendance(self):
        return 500
