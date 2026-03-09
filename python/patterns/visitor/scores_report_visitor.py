from visitor import Visitor


class ScoresReportVisitor(Visitor):
    def visit_intramural(self, intramural):
        print()
        print('SCORES REPORT')
        
    def visit_sport(self, sport):
        print()
        print(' ', sport.type)
        
    def visit_game(self, game):
        winner = game.winner
        winner_points = game.winner_points
        loser = game._loser
        loser_points  = game._loser_points
        
        print(f'{winner.name:>14s} beat '
              f'{loser.name:>10s}'
              f'{winner_points:3d} to '
              f'{loser_points:2d}')
    
    def visit_hall(self, hall):
        return
