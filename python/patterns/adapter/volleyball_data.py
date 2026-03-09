from game_data import GameData


class VolleyballData(GameData):
    def __init__(self, volleyball_stats):
        self._volleyball_stats = volleyball_stats

    def report_venue(self):
        return self._volleyball_stats.stats['venue']
    
    def report_attendance(self):
        return self._volleyball_stats.stats['attendance']
