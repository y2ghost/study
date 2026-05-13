from node import Node


class Game(Node):
    def __init__(self, winner, winner_points,
                       loser,  loser_points):
        self._winner = winner
        self._winner_points = winner_points
        self._loser = loser
        self._loser_points = loser_points
        
        winner.increment_win_count()
        
    @property
    def winner(self):
        return self._winner
        
    @property
    def winner_points(self):
        return self._winner_points
        
    @property
    def loser(self):
        return self._loser
        
    @property
    def loser_points(self):
        return self._loser_points
    
    def accept(self, visitor):
        visitor.visit_game(self)

        
